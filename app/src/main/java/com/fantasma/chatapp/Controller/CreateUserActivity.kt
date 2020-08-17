package com.fantasma.chatapp.Controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.fantasma.chatapp.R
import com.fantasma.chatapp.Services.AuthServices
import com.fantasma.chatapp.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {
    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        userAvatar = if(color == 0) {
            "light$avatar"
        } else {
            "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View) {
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)

        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR,  $savedG, $savedB, 1]"
    }

    fun createUserClicked(view: View) {
        enableSpinner(true)

        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        if(userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Make sure user name, email, and password are filled in.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
            return
        }

        AuthServices.registerUser(email, password) {registerSuccess ->
            if(registerSuccess) {
                AuthServices.loginUser(email, password) {loginSuccess ->
                    if(loginSuccess) {
                        AuthServices.createUser(userName, email, userAvatar, avatarColor) {createUserSuccess ->
                            if(createUserSuccess) {

                                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                enableSpinner(false)
                                finish()
                            } else {
                                errorToast()
                            }
                        }
                    } else {
                        errorToast()
                    }
                }
            } else {
                errorToast()
            }
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, please try again.", Toast.LENGTH_LONG).show()

        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if(enable) {
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.INVISIBLE
        }
        createUserBtn.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backgroundColorBtn.isEnabled = !enable
    }

}
