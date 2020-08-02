# Android-Update-Server

Easy to setup server that manages updates for your Android app which cannot be distributed via Google Play or you just simply don't want to use it.

# Server installation
1. Create new project on [Firebase](https://console.firebase.google.com/)
2. [Create and Download `google-services.json` from here](https://cloud.google.com/docs/authentication/production#manually)
3. [![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)
4. That's it you have your own update server running

# Android part
When you have your server running only thing left is to include android library into your app, so you can be automatically notified when you upload a new version of your app.
Find out how [here](https://github.com/p1-ro/android-update-lib).
