# Introduction #

This is an android project.
It is a music player, on android version 2.2


# Details #

Function details:
  * it support **mp3** format.
  * it support play **list**.
  * it support **_lrc sync_** view.
  * it support seek bar.
    * android nativate feature.
    * show the duration time at the end of seek bar.


# Program Design #

Using Binder IPC:
  * Design details:
    * service is in a **IPC** prcess usign onTransact().
    * play, next, prev, pause, using transact() in client.