package com.example.developerslife

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var mStack = ArrayDeque<Pair<String, String>>()
    private var mTextView: TextView? = null
    private var mImageView: ImageView? = null
    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mImageView = imageView
        mTextView = textView
        mProgressBar = progressBar
        prevButton.isEnabled = false
        onClickNext(nextButton);
    }

    fun onClickNext(view: View?) {
        imageView.visibility = View.GONE
        textView.visibility = View.GONE
        progressBar.visibility = ProgressBar.VISIBLE;
        val url = "https://developerslife.ru/random?json=true"
        AsyncRequest(this).execute(url)
    }

    fun onClickBack(view: View?) {
        imageView.visibility = View.GONE
        textView.visibility = View.GONE
        progressBar.visibility = ProgressBar.VISIBLE;
        mStack.removeLast()
        if (mStack.size == 1) {
            prevButton.isEnabled = false
        }
        var url = mStack.last().first
        var description = mStack.last().second
        Glide
            .with(this)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    TODO("Not yet implemented")
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mProgressBar?.visibility = ProgressBar.GONE
                    mTextView?.text = description
                    if (mStack.size > 1) {
                        prevButton.isEnabled = true
                    }
                    mImageView?.visibility = View.VISIBLE
                    mTextView?.visibility = View.VISIBLE
                    return false
                }
            })
            .into(mImageView!!)
    }

    class AsyncRequest internal constructor(context: MainActivity) : AsyncTask<String?, Int, String?>() {
            private var mTextView: TextView? = context.textView
            private var mImageView: ImageView? = context.imageView
            private var mProgressBar: ProgressBar? = context.progressBar
            private var mPrevButton: Button = context.prevButton
            private var mStack = context.mStack
            lateinit var mGlide : Glide
            private val mContext: Context? = context

            @Throws(IOException::class, MalformedURLException::class)
            fun HTTPGetCall(WebMethodURL: String?): String? {
                val response = StringBuilder()

                //Prepare the URL and the connection
                val u = URL(WebMethodURL)
                val conn: HttpURLConnection = u.openConnection() as HttpURLConnection
                if (conn.responseCode === HttpURLConnection.HTTP_OK) {
                    //Get the Stream reader ready
                    val input = BufferedReader(InputStreamReader(conn.inputStream), 8192)

                    //Loop through the return data and copy it over to the response object to be processed
                    var line: String? = null
                    while (input.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    input.close()
                }
                return response.toString()
            }

            override fun doInBackground(vararg arg: String?): String? {
                return try {
                    HTTPGetCall(arg[0])
                } catch (exception : Exception) {
                    "Error"
                }
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                val obj = JSONObject(result)
                mStack.addLast(Pair(obj.getString("gifURL"), obj.getString("description")))
                if (mContext != null) {
                    Glide
                        .with(mContext)
                        .load(obj.getString("gifURL"))
                        .override(1000, 1000)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                TODO("Not yet implemented")
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: com.bumptech.glide.load.DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                mProgressBar?.visibility = ProgressBar.GONE
                                mTextView?.text = obj.getString("description")
                                if (mStack.size > 1) {
                                    mPrevButton.isEnabled = true
                                }
                                mImageView?.visibility = View.VISIBLE
                                mTextView?.visibility = View.VISIBLE
                                return false
                            }
                        })
                        .into(mImageView!!)
                }
            }
        }
}