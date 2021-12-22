# EasyShoot

[![](https://jitpack.io/v/evd-evanss/easyshoot.svg)](https://jitpack.io/#evd-evanss/easyshoot)

## Capture and Share Images Easily

### This is a project to facilitate capturing and sharing screens in android development

## Get Started Developing

### 1 - Add maven to build.gradle at project level
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

### 2 - Implement latest version of easyshoot to build.gradle at app level
```
dependencies {
    ...
    implementation 'com.github.evd-evanss:easyshoot:1.0.4'
}
```

### 3 - Include the following permission in the manifest
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

### 4 - Create an instance of easy shoot
```
val easyShoot = EasyShoot.Builder(context)
    .requestPermission()
    .setViewForScreenShoot(root)
    .build()
```

### 5 - Call the function to capture and share
```
easyShoot.takeAndShareScreenShoot()
```
