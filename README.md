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