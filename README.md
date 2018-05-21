# 10 Darts sample project #

Sample project that integrates the [10Darts library](https://github.com/10darts/android-TendartsSDK). The purpose of this project is double. On the one hand, to create an app that integrates the SDK and allows you to test the different functionalities it offers, and on the other hand, to have the possibility to work on the SDK module directly on the same project.

## Index ##

- [Requirements](#requirements)
- [Build variants](#build-variants)
- [Building considerations](#building-considerations)
- [SDK integration steps](#sdk-integration-steps)
- [Common problems](#common-problems)

## Requirements ##

* Android 4.4(19) or higher
* Andriod Studio 3.0.1 or higher

## Build variants ##

The project has been set up with 4 Gradle build variants:

- **sdkDebug**:
  - Server: production
  - 10darts account: auroralabs
  - App name: ß 10darts Production
- **sdkTestingDebug**:
  - Server: staging
  - 10darts account: auroralabs
  - App name: ß 10darts Staging
- **sdkRelease**:
  - Server: production
  - 10darts account: arena@10darts.com
  - App name: 10darts Production
- **sdkTestingRelease**:
  - Server: staging
  - 10darts account: arena@10darts.com
  - App name: 10darts Staging

## Building considerations ##

When compiling for **Release** build variants a signing config will be required. The project is set to pick the kestore credentials from a file in [certs/release/cert_release.properties](certs/release/cert_release.properties).
That file won't be uploaded to the repo, so if you don't have it, you can create it with the content:

```
storePassword=<replace with data>
keyAlias=<replace with data>
keyPassword=<replace with data>
```

For the Maven deployer tasks credentials are needed. The project is set to pick them from a file in [sdk/credentials.properties](sdk/credentials.properties).
That file won't be uploaded to the repo, so if you don't have it, you can create it with the content:

```
bintray.user=<replace with data>
bintray.apikey=<replace with data>
bintray.gpg.password=<replace with data>
```

## SDK integration steps ##

We have a [setup guide](https://docs.10darts.com/tutorials/android/setup.html) and a [guide for GCM](https://docs.10darts.com/faq/android/gcm.html).

However there is a section that is not entirely clear, it is necessary to create an app in the dashborad of 10 Darts:

![IMAGE](docs/img/dashboard_create_app.png)

## Common problems ##

If you have conflicts with Google Play Services version, you can exclude it from 10darts and add it manually with the version you need:

```
    implementation  ('com.10darts:sdk:1.26') {
        exclude group: 'com.google.android.gms', module: 'play-services-location'
        exclude group: 'com.google.android.gms', module: 'play-services-gcm'
    }

    implementation "com.google.android.gms:play-services-location:15.0.1"
    implementation ("com.google.android.gms:play-services-gcm:15.0.1")
```

Keep in mind that at least, it should be the version that we define [here](https://github.com/10darts/android-TendartsSDK/blob/development/build.gradle) to prevent problems related with missing API methods.


