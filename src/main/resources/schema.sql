CREATE TABLE IF NOT EXISTS Role (
    id     			TINYINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Уникальный идентификатор роли пользователя',
    version         INTEGER NOT NULL,
    role        	VARCHAR(25) UNIQUE COMMENT 'Название роли пользователя'
)
COMMENT 'Роль пользователя';

CREATE TABLE IF NOT EXISTS User (
    id     			    INTEGER PRIMARY KEY AUTO_INCREMENT COMMENT 'Уникальный идентификатор пользователя',
    version             INTEGER NOT NULL,
    user_name			VARCHAR(50) NOT NULL UNIQUE COMMENT 'Имя пользователя',
    password		    VARCHAR(50) NOT NULL COMMENT 'Пароль пользователя',
    email               VARCHAR(50) NOT NULL COMMENT 'E-mail',
    code			    VARCHAR(100) COMMENT 'Код регистрации',
    registration_date   DATETIME COMMENT 'Дата регистрации',
    is_confirmed   	    BOOLEAN COMMENT 'Статус учетной записи (подтверждена или нет)'
)
COMMENT 'Пользователь';

CREATE TABLE IF NOT EXISTS User_Role (
	id     			INTEGER PRIMARY KEY AUTO_INCREMENT
	COMMENT 'Уникальный идентификатор таблицы отношений пользователей и ролей',
    user_id   		INTEGER COMMENT 'Уникальный идентификатор пользователя, внешний ключ',
    role_id   	    TINYINT COMMENT 'Уникальный идентификатор роли, внешний ключ',
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Role (id) ON DELETE CASCADE ON UPDATE CASCADE
)
COMMENT 'Таблица для отношения ManyToMany пользователей и ролей';

CREATE TABLE IF NOT EXISTS File (
    id     			INTEGER PRIMARY KEY AUTO_INCREMENT COMMENT 'Уникальный идентификатор файла',
    version         INTEGER NOT NULL,
    file_name	    VARCHAR(200) COMMENT 'Название файла',
    download_count	INTEGER COMMENT 'Количество скачиваний',
    user_id     	INTEGER COMMENT 'Уникальный идентификатор пользователя, внешний ключ',
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE ON UPDATE CASCADE
)
COMMENT 'Файл';

CREATE TABLE IF NOT EXISTS Access (
	user_id			INTEGER NOT NULL COMMENT 'Уникальный идентификатор пользователя, внешний ключ',
    subscriber_id	INTEGER NOT NULL COMMENT 'Уникальный идентификатор пользователя, запрашивающего доступ к файлу, внешний ключ',
    version         INTEGER NOT NULL,
    read_access   	BOOLEAN COMMENT 'Доступ на чтение',
    download_access BOOLEAN COMMENT 'Доступ на скачивание',
    request			BOOLEAN COMMENT 'Запрос',
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (subscriber_id) REFERENCES User (id) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user_id, subscriber_id)
)
COMMENT 'Доступ к файлам для других пользователей';

CREATE INDEX IX_User_Code ON User (code);
