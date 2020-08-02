# Android-Update-Server

Easy to set up server that manages updates for your Android app which cannot be distributed via Google Play or you just simply don't want to use it.

## Server Installation
1. Create new project on [Firebase](https://console.firebase.google.com/)
2. [Create and Download `google-services.json` from here](https://cloud.google.com/docs/authentication/production#manually)
3. [![Deploy](https://www.herokucdn.com/deploy/button.svg)](https://heroku.com/deploy)
4. That's it. You have your own update server running.

## Android Part
When you have your server running only thing left is to include android library into your app, so you can be automatically notified when you upload a new version of your app.
Find out how [here](https://github.com/p1-ro/android-update-lib).

## Automatic Deployments

You can integration publishing new version of app into your CI simply by calling `/api/upload` like this:

```bash
curl -s -H "apiKey: YOUR_API_KEY" -F "file=@PATH_TO_LATEST_APK" \ 
  -X POST https://YOUR_HEROKU_PREFIX.herokuapp.com/api/upload
```