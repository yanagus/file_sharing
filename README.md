# Многопользовательский сервис хранения файлов</br>
Java, Spring Boot, MySQL, FreeMarker, Bootstrap</br>
В приложении предусмотрены 3 роли: "Пользователь", "Администратор", "Аналитик".</br>
Главная страница:
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/main.png)

Форма регистрации нового пользователя:
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/reg_form.png)
Форма регистрации нового пользователя, если данные не введены:
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/reg_form2.png)
При успешной регистрации выдается сообщение, что необходимо перейти по ссылке отправленной на email.
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/activate.png)
Ссылка действительна в течение 24х часов. По прошествии большего времени с момента регистрации, выдается сообщение:
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/link_expired.png)
При успешной активации выдается форма логина с сообщением, что учетная запись активирована
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/successfully_activated.png)

Пользователь может загружать и скачивать загруженные файлы. Удалять можно только свои файлы.</br>
Пользователь может дать доступ другому пользователю сервиса на просмотр списка своих файлов или на скачивание.</br>
Пользователь может запросить доступ на просмотр списка или на скачивание файлов другого пользователя.
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/no_access.png)


Пользователь с ролью "Администратор" может просматривать и удалять файлы всех пользователей.
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/all_files_admin.png)

Пользователь с ролью "Аналитик" может просматривать все файлы,
а также может получить статистику сколько пользователей в системе,
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/analyst_statistic1.png)
сколько у какого пользователя файлов
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/analyst_statistic2.png)
и сколько раз какой файл был скачан.
![Image alt](https://github.com/yanagus/file_sharing/raw/master/image/analyst_statistic3.png)



