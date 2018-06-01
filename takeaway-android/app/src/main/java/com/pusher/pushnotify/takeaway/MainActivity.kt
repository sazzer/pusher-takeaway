package com.pusher.pushnotify.takeaway

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.messaging.RemoteMessage
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.pusher.pushnotifications.PushNotificationReceivedListener
import com.pusher.pushnotifications.PushNotifications
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var recordAdapter: MenuItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PushNotifications.start(getApplicationContext(), "BEAMS_INSTANCE_ID")
    }

    override fun onResume() {
        super.onResume()
        recordAdapter = MenuItemAdapter(this)
        val recordsView = findViewById<View>(R.id.records_view) as ListView
        recordsView.setAdapter(recordAdapter)

        refreshMenuItems()
        receiveNotifications()
    }

    private fun receiveNotifications() {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("takeaway",
                    "Pusher Takeaway",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        Log.i("Notifications", "Ready to process notifications")
        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, object : PushNotificationReceivedListener {
            override fun onMessageReceived(remoteMessage: RemoteMessage) {
                Log.i("Notification", remoteMessage.data.toString())

                val pending = remoteMessage.data["itemsPending"]?.toInt() ?: 0
                val started = remoteMessage.data["itemsStarted"]?.toInt() ?: 0
                val finished = remoteMessage.data["itemsFinished"]?.toInt() ?: 0

                val total = pending + started + finished

                val notification = when(remoteMessage.data["status"]) {
                    "STARTED" -> {
                        NotificationCompat.Builder(applicationContext, "takeaway")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Your order")
                                .setContentText("Your order is being cooked")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setProgress(total, finished, finished == 0)
                    }
                    "COOKED" -> {
                        NotificationCompat.Builder(applicationContext, "takeaway")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Your order")
                                .setContentText("Your order is ready")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setProgress(total, total, false)
                    }
                    "OUT_FOR_DELIVERY" -> {
                        NotificationCompat.Builder(applicationContext, "takeaway")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Your order")
                                .setContentText("Your order is out for delivery")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    }
                    "DELIVERED" -> {
                        NotificationCompat.Builder(applicationContext, "takeaway")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Your order")
                                .setContentText("Your order is outside")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    }
                    else -> null
                }

                notification?.let {
                    notificationManager.notify(0, it.build())
                }
            }
        })
    }

    private fun refreshMenuItems() {
        val client = AsyncHttpClient()
        client.get("http://10.0.2.2:8080/menu-items", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, response: JSONArray) {
                super.onSuccess(statusCode, headers, response)
                runOnUiThread {
                    val menuItems = IntRange(0, response.length() - 1)
                            .map { index -> response.getJSONObject(index) }
                            .map { obj ->
                                MenuItem(
                                        id = obj.getString("id"),
                                        name = obj.getString("name")
                                )
                            }

                    recordAdapter.records = menuItems
                }
            }
        })
    }

    fun placeOrder(view: View) {
        val items = recordAdapter.order
        if (items.isEmpty()) {
            Toast.makeText(this, "No items selected", Toast.LENGTH_LONG)
                    .show()
        } else {

            val request = JSONArray(items)

            val client = AsyncHttpClient()
            client.post(applicationContext, "http://10.0.2.2:8080/orders", StringEntity(request.toString()),
                    "application/json", object : JsonHttpResponseHandler() {

                override fun onSuccess(statusCode: Int, headers: Array<out Header>, response: JSONObject) {
                    val id = response.getString("id")
                    PushNotifications.subscribe(id)

                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Order placed", Toast.LENGTH_LONG)
                                .show()
                    }
                }
            });
        }
    }
}
