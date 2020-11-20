# otus-android-basic-course

## Homework #1
ENG:
Getting started on the movie search application. Create a project and movie pages.
1. Create a project
2. Push the project on GitHub
3. Add a project description and ReadMe.MD
4. On the first screen of your application, create some pictures with movies. Add a movie title and a details button to each picture. Any images, title, and description
5. By clicking on Details - set a different color for the button.
6. Saving the selected movie when changing orientations and when returning from the second screen
7. Add the “Invite a Friend” button and send the invitation of your choice.
8. *Add a Like icon and a text box for the comments on the second screen. Returning the value of the checkbox and the text of the comment when switching back to the first screen in console (use log.i/d). Checkbox and text comment return values

RUS:
Начало работы над приложением по поиску фильмов. Создание проекта и страницы с фильмами.
1. Создайте проект
2. Залейте проект на GitHub
3. Добавьте описание проекта в заголовке и Read.me
4. На первом экране своего приложения создайте несколько картинок с фильмами.
К каждой картинке добавьте название фильма и кнопку “Детали”. Изображения любые, название и описание произвольные
5. По нажатию на Детали - выделяйте другим цветом название выбранного фильма, открывайте новое окно, где показывайте картинку и описание фильма
6. Сохраняйте выделение фильма при повороте и при возвращении со второго экрана
7. Добавьте кнопку “Пригласить друга” и отправляйте приглашение по вашему выбору (почта, смс, социальные сети)
8. *Добавьте на втором экране checkbox “Нравится” и текстовое поле для комментария. Возвращайте значение чекбокса и текст комментария при переходе обратно на первый экран. Возвращенные значения чекбокса и текстового комментария выводим в лог


## Homework #2
ENG:
Using themes and qualifiers.
1. Create different styles for title text and description
2. Use styles on the main screen and movie detail screen
3. Add support for English and Russian text
4. Use the vector image from the standard set for the invite a friend button
5. Add landscape support. The interface should be different. For example, in portrait 2 films in the list line, and in landscape 4
6. Create a custom confirmation dialog when you exit the application by pressing the back button (use the onBackPressed method)
7. * Add a button to switch themes in the application, for example, day/night

RUS:
Использование тем и квалификаторов.
1. Создайте различные стили для текста заголовка и описания 
2. Используйте стили на экране со списком и детальном экране 
3. Добавьте поддержку английского и русского языков для элементов интерфейса, например, для кнопки "детали" и "пригласить друга" 
4. Используйте векторное изображение из стандартного набора для кнопки пригласить друга 
5. Добавьте поддержку альбомной ориентации. Интерфейс должен отличаться. Например, в портретной 2 фильма в строке списка, а в альбомной 4 
6. Создайте кастомный диалог подтверждения при выходе из приложения при нажатии кнопки back (использовать метод onBackPressed) 
7. * Добавьте кнопку переключения темы в приложении, например дневной\ночной 

P.S. Задание со звездочкой * - повышенной сложности. Если вы с ним не справитесь - ничего страшного, оно не является обязательным. 


## Homework #3
ENG:
Work with RecyclerView.
1. Switch your application to display lists using RecyclerView
2. Add functionality to your application by saving movies to your favorites list (for now, keep your favorites in a List at the Activity level).
To do this, use either a long press on the list item or tap on the ImageView (like icon) in the movie activity
3. Create a screen where the Favorites list will be displayed
4. Make it so that you can add items to the list and delete them (Only delete)
5. * Write your own ItemDecoration
6. * Explore ReciclerView.ItemAnimator yourself, create your own animations

P.S. Task with  * - increased complexity. If you can’t deal with it, it’s okay, it is not obligatory.
Evaluation Criteria: +1 point per attempt
+1 point for completing the main task
+1 point for completing a task with *


RUS:
Работа со списками.
1. Переведите ваше приложение на отображение списков с помощью RecyclerView 
2. Дополните функционал вашего приложения сохранением фильмов в список избранного (избранное пока храните в обычном List на уровне Activity). 
Используйте для этого или долгое нажатие на элемент списка, или тап на ImageView в виде сердечка рядом с названием фильма
3. Создайте экран, где будет отображаться список Избранного 
4. Сделайте так, чтобы в список можно было добавлять элементы и удалять их (Только удалять)
5. * Написать собственный ItemDecoration
6. * Самостоятельно изучите ReciclerView.ItemAnimator, создайте свои собственные анимации

P.S. Задание со звездочкой * - повышенной сложности. Если вы с ним не справитесь - ничего страшного, оно не является обязательным.
Критерии оценки: +1 балл за попытку
+1 балл за выполнение основного задания
+1 балл за выполнения задания со звездочкой * 


## Homework #4
ENG:
Fragments and navigation.
1. Change your application to a single Activity and fragments
2. Use NavigationDrawer or BottomNavigation to navigate between fragments.
3. Add CoordinatorLayout + CollapsingToolbar to the movie detail screen
4. Add a Snackbar or Toast, indicating the success of adding / removing from favorites
5. * Add undo option to snackbar in favorite list

RUS:
Фрагменты и навигация.
1. Переведите свое приложение на единственную Activity и несколько фрагментов  (https://medium.com/androiddevelopers/the-android-lifecycle-cheat-sheet-part-i-single-activities-e49fd3d202ab)
2. Для навигации между фрагментами используйте NavigationDrawer или BottomNavigation (https://habr.com/ru/post/449776/), (https://material.io/components/bottom-navigation), (https://androidwave.com/bottom-navigation-bar-android-example/), (https://ahsensaeed.com/bottom-navigation-view-android-example/)
3. Добавьте CoordinatorLayout + CollapsingToolbar на детальный экран фильма 
4. Добавьте Snackbar или Toast, сообщающий об успехе добавления\удаления из избранного 
5. * Добавьте возможность отмены действия в snackbar 

P.S. Задание со звездочкой * - повышенной сложности. Если вы с ним не справитесь - ничего страшного, оно не является обязательным.
Критерии оценки: +1 балл за попытку
+1 балл за выполнение основного задания
+1 балл за выполнения задания со звездочкой

Идеи:
- Добавить  Bottom Sheet Dialog, чтобы быстро просматривать про фильм

Заметки:

```kotlin
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) { //Check if bundle null (Init for first time)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MovieListFragment(), MovieListFragment.TAG)
                .commit()
            initViews()
            initClickListeners()
        } else { //Restore last screen from backstack
            val fragment = supportFragmentManager.fragments.last()
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment, fragment.tag)
                .commit()
        }
        //...Other init like initViews(), initClickListeners()
    }
```

## Homework #5
ENG:
Connecting the application to the Internet.
1. Connect the movie server API and download movie data from the server when the application starts. At the load indicator of data
2. Use Glide to upload images
3. Add pagination (loading new films when scrolling)
4. * Add pull to refresh

RUS:
Подключение приложения к интернету.
1. Подключите API сервера с фильмами и загружайте данные о фильмах с сервера при запуске приложения. Во время загрузки должен отображаться индикатор на ваш выбор +
2. Используйте Glide для загрузки изображений +
3. Добавьте пагинацию (подгрузку новых фильмов при скролле) +
4. * Добавьте pull to refresh

Что было прочитано:
https://proandroiddev.com/the-real-repository-pattern-in-android-efba8662b754 (Рассказ о правильной архитектуре repository pattern)
https://medium.com/nuances-of-programming/%D1%88%D0%B0%D0%B1%D0%BB%D0%BE%D0%BD-repository-%D0%B2-android-a561d4bbd9ee
https://medium.com/@bapspatil/caching-with-retrofit-store-responses-offline-71439ed32fda
https://startandroid.ru/ru/uroki/vse-uroki-spiskom/490-urok-181-constraintslayout-advanced.html (Работа с constraintslayout)
https://riptutorial.com/android/example/20493/material-linear-progressbar
https://stackoverflow.com/questions/42301818/retrofit-2-how-to-show-progress-bar-on-receiving-json-response
https://habr.com/ru/post/314028/ (Retrofit)
https://material.io/components/progress-indicators#usage
https://shurikus57.github.io/post/2018/04/lint_in_android_studio/ (Code Quality)
https://stackoverflow.com/questions/48151918/how-to-cache-images-in-glide (Thread about cache types)
https://medium.com/@ankit.sinhal/handler-in-android-d138c1f4980e
https://guides.codepath.com/android/implementing-pull-to-refresh-guide

Улучшение на будущее:
https://developer.android.com/topic/libraries/architecture/paging


## Homework #6
ENG:
Architectural components (LiveData, ViewModel, lifecycle-aware components)

RUS:
Архитектурные компоненты.
1. Переведите свое приложение на архитектурные компоненты и MVVM
2. Обменивайтесь данными между фрагментами используя ViewModel
3. Добавьте отображение ошибок, в случае ошибки сервера или отсутствия интернета
4. Добавьте возможность повторить запрос в случае ошибки
5. * Создайте ViewModel с помощью фабрики

Что было прочитано:
https://github.com/android/architecture-samples
Прочитать: https://www.toptal.com/android/android-apps-mvvm-with-clean-architecture
https://github.com/igorwojda/android-showcase
https://android.jlelse.eu/lets-keep-activity-dumb-using-livedata-53468ed0dc1f
https://startandroid.ru/ru/courses/dagger-2/27-course/architecture-components/527-urok-4-viewmodel.html
https://medium.com/@bapspatil/caching-with-retrofit-store-responses-offline-71439ed32fda

```text
ViewModel - здесь удобно держать все данные, которые нужны вам для формирования экрана. Они будут жить при поворотах экрана, но умрут, когда приложение будет убито системой.

onSavedInstanceState - здесь нужно хранить тот минимум данных, который понадобится вам для восстановления состояния экрана и данных в ViewModel после экстренного закрытия Activity системой. Это может быть поисковый запрос, ID и т.п.

Соответственно, когда вы достаете данные из savedInstanceState и предлагаете их модели, это может быть в двух случаях:

1) Был обычный поворот экрана. В этом случае ваша модель должна понять, что ей эти данные не нужны, потому что при повороте экрана модель ничего не потеряла. И уж точно модель не должна заново делать запросы в БД, на сервер и т.п.

2) Приложение было убито, и теперь запущено заново. В этом случае модель берет данные из savedInstanceState и использует их, чтобы восстановить свои данные. Например, берет ID и идет в БД за полными данными.
```

https://blog.mindorks.com/understanding-livedata-in-android

https://medium.com/mindorks/livedata-viewmodel-making-your-own-magic-73facb06fbb
LiveData - класс который содержит данные и работает по принципу паттерна Наблюдатель, поддерживает  жизненный цикл lifeCycleOwner(активити, фрагмент)
ViewModel - класс пощник (где-то его называют контейнером), помогает нам хранить, управлять и подготавливать данные, относящиеся к пользовательскому интерфейсу, с учетом жизненного цикла.
https://medium.com/mindorks/livedata-viewmodel-making-your-own-magic-73facb06fbb

Про вопросо UseCase и Interactors:
It introduces a new layer called domain where the Use Cases (also called Interactors) live. 
The domain layer is where the business logic happens, which is the code that determines what 
the app does with the data coming from the repository before it's exposed to the UI for display.

## Homework #7
ENG:
Data storage with Room.

RUS:
Хранение данных.
1. Сохраняйте полученные данные фильмов в кэш (базу данных Room) +
2. Сохраняйте список избранного в базе данных +
3. Во время загрузки фильмов отображайте данные из кэша +
4. Храните время последнего запроса данных и, если прошло меньше 20 минут, отображайте кэш, запрос в сеть идти не должен. Для хранения времени используйте SharedPreferences +
5*. В случае ошибки сервера \ отсутствия интернета отобразите snackbar с ошибкой и кнопкой "попытаться снова\обновить" +

Что было прочитано?
https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/529-urok-5-room-osnovy.html
https://github.com/googlecodelabs/android-room-with-a-view
https://github.com/DimaKoz/DemoRoom/blob/master/app/build.gradle
https://devcolibri.com/7-pro-tips-for-room/
https://quares.ru/?id=2937 - Running on another thread but still blocking the main thread
https://ru.stackoverflow.com/questions/470899/%D0%94%D0%BB%D1%8F-%D1%87%D0%B5%D0%B3%D0%BE-%D0%BD%D1%83%D0%B6%D0%BD%D0%B0-%D0%B1%D0%B8%D0%B1%D0%BB%D0%B8%D0%BE%D1%82%D0%B5%D0%BA%D0%B0-eventbus-%D0%B8%D0%BB%D0%B8-%D0%B5%D1%91-%D0%B0%D0%BD%D0%B0%D0%BB%D0%BE%D0%B3-otto -про ЕвентБас



## Homework #8
ENG:
Working with AlarmManager. New functionality were added:
1. Add to the application the functionality of adding a movie to the "Watch Later" list
2. When adding, you must set the date of the reminder
3. When the required date comes, a push notification should appear
4. Clicking on the push notification should open the movie's detailed page
5. * Add a "Watch Later" list display to the application where you can change the date of the reminder

RUS:
1. Добавьте в приложение функционал добавление фильма в список "Посмотреть позже" +
2. При добавлении необходимо указывать дату напоминания +
3. При наступлении необходимой даты должно появляться пуш уведомление +
4. Переход по пуш уведомлению должен открывать детальную страницу фильма +
5.* Добавьте в приложение отображение списка "Посмотреть позже", в котором можно будет изменять дату напоминания +

Что было прочитано?
https://developer.android.com/guide/components/services?hl=ru
https://habr.com/ru/post/186434/ - Tasks и Back Stack в Android
https://habr.com/ru/company/jugru/blog/351166/ - Руководство по фоновой работе в Android. Часть 3: Executors и EventBus
https://hackernoon.com/how-to-use-new-material-date-picker-for-android-s7k32w0
https://www.youtube.com/watch?v=D0VpASTpgmw - объясняет пример по работе с datetimepiker
https://stackoverflow.com/questions/46728857/kotlin-parcelable-class-throwing-classnotfoundexception/46766285 - Была проблема с передачей parcelable объекта. Потребоволась применять bundle. В дебагере посмотрел было имя другое у бандла.
https://startandroid.ru/ru/uroki/vse-uroki-spiskom/190-urok-116-povedenie-activity-v-task-intent-flagi-launchmode-affinity.html - про флаги интентов
https://stackoverflow.com/questions/35451309/open-fragment-from-notification-when-the-app-in-background - как открывать фрагмент через нотификацию
https://developer.android.com/training/scheduling/alarms#boot - Start an alarm when the device restarts

## Homework #9
Сервисы Firebase.
Цель: +1 балл за попытку
+ 1. Добавьте crashlytics в свое приложение //Добавил Events во класс MovieFragment
+ 2. Добавьте в приложение Firebase Cloud Messaging
3. Добавьте пуш уведомление из FCM, которое будет содержать всю информацию о фильме и, соответственно, открывать её при клике
* 4. Добавьте Remote Config в свое приложение и передавайте какие-нибудь данные. Например, "категорию фильма по-умолчанию",
чтобы удаленно можно было задать, какой список фильмов грузить (топ за неделю, топ за все время, свежие и т.д.)

Что было прочитано?
https://firebase.google.com/docs/crashlytics/test-implementation?authuser=0&platform=android - Протестируйте свою реализацию Crashlytics
```
adb shell setprop log.tag.FirebaseCrashlytics DEBUG
adb logcat -s FirebaseCrashlytics
adb shell setprop log.tag.FirebaseCrashlytics INFO
```

https://firebase.google.com/docs/crashlytics/get-started?authuser=0&platform=android - Подключение Crashlytics
https://support.google.com/firebase/answer/6317498?hl=en&ref_topic=6317484 - Описание Events: All apps для FirebaseAnalytics
https://guides.codepath.com/android/Understanding-the-Android-Application-Class - Understanding the Android Application Class

Работа с Events
```
adb devices
adb shell setprop log.tag.FA VERBOSE
adb shell setprop log.tag.FA-SVC VERBOSE
adb logcat -v time -s FA FA-SVC
```


DebugView - очень долгий update у меня был (и большая часть event не доходила.)
```
Чтобы включить режим отладки Google Analytics на устройстве Android, выполните следующие команды:

adb shell setprop debug.firebase.analytics.app artsok.github.io.movie4k

Это поведение сохраняется до тех пор, пока вы явно не отключите режим отладки, выполнив следующую команду:

adb shell setprop debug.firebase.analytics.app .none.
```

Если мы хотим, чтобы нотификация от FCM открывала нужную активити, когда приложение в background, killed, то требуется отправлять данные data, а не notification.
Описание запроса, который уходит:
https://firebase.google.com/docs/reference/fcm/rest/v1/projects.messages#Message


Пример запроса:
```
POST /v1/projects/movies-d5387/messages:send HTTP/1.1
Host: fcm.googleapis.com
Content-length: 266
Content-type: application/json
Authorization: Bearer ya29.a0AfH6SMD_x4TZKFZMqOpF_S_MHQ4UufaS2yuC0RH7gVkf2buNCKCKJoyMUxr-AjScWvzd1DYKijbYYuzcYemVXGK0EE4nnoGmxDAJgjgkmMg3-U4sphqcPU8fK0SmoiZZAtqpyBnIKfAFJLar6Uxpoq5FL35Ij3UXsYDeWmu3ghg
{
  "message":{
    "data": {
     "movie" : "766165",
     "text" : "etc"
   },
    "token" : "c753C6ZQSKCd2AA58ipq18:APA91bHG9psCIhobqGN3kqWcmh8mzXh5b434XxI4HZwCe2emm-aygVqxXufwsoy1Nbepgku6aAENht6XtO3DVEiA2oMpuVKjrmKQdsxjymQPizCEGcym8c4kcsrf0d8QoLz2JAZ0H9Fe"
  }
}
```

Пример ответа:

```
HTTP/1.1 200 OK
Content-length: 83
X-xss-protection: 0
X-content-type-options: nosniff
Transfer-encoding: chunked
Vary: Origin, X-Origin, Referer
Server: ESF
-content-encoding: gzip
Cache-control: private
Date: Fri, 20 Nov 2020 19:26:10 GMT
X-frame-options: SAMEORIGIN
Alt-svc: h3-29=":443"; ma=2592000,h3-T051=":443"; ma=2592000,h3-Q050=":443"; ma=2592000,h3-Q046=":443"; ma=2592000,h3-Q043=":443"; ma=2592000,quic=":443"; ma=2592000; v="46,43"
Content-type: application/json; charset=UTF-8
{
  "name": "projects/movies-d5387/messages/0:1605900370647291%73a642e8f9fd7ecd"
}
```

Использовал сервис для отладки:
https://developers.google.com/oauthplayground/?code=4/0AY0e-g55d7lCLHU7CJa-IlFWkMkYl0W_3_39ITQlmKrXRf6lkp3qd9QffAXlJxw17OktDg&scope=email%20https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/firebase.messaging%20openid&authuser=0&prompt=consent


Получить FCM Token:
https://firebase.google.com/docs/cloud-messaging/android/client#retrieve-the-current-registration-token
https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase/37845174 - How to handle notification when app in background in Firebase
https://blog.mestwin.net/send-your-test-fcm-push-notification-quickly-with-curl/ - Send your test FCM push notification with cURL


Интересно про Dagger:
https://medium.com/@marco_cattaneo/android-viewmodel-and-factoryprovider-good-way-to-manage-it-with-dagger-2-d9e20a07084c
https://proandroiddev.com/forget-rxjava-kotlin-coroutines-are-all-you-need-part-1-2-4f62ecc4f99b

Note: Initially for me also not worked,After Seeing many posts i realized that the pending intent to be canceled should be same as the
original pending intent that was used to schedule alarm. The pending intent to be cancelled should have set to same action and same data fields,
if any have those were used to set the alarm. After setting the same ACTION and data values though i'm not using them,only cancelled the Alarm.