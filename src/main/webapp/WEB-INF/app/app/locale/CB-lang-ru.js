Ext.define("CB.locale.ru.view.login.Home", {
    override: "CB.view.login.Home",
    title: "Вход в систему",
    columnName: "Название"
});
Ext.define("CB.locale.ru.view.cabinet.Home", {
    override: "CB.view.cabinet.Home",
    fldMessage: "Личный кабинет"
});
Ext.define("CB.locale.ru.view.error.Home", {
    override: "CB.view.error.Home",
    fldMessage: "Упс, что-то случилось"
});
Ext.define("CB.locale.ru.view.registration.StepOne", {
    override: "CB.view.registration.StepOne",
    title: "ФИО и номер мобильного телефона"
});
Ext.define("CB.locale.ru.view.registration.StepTwo", {
    override: "CB.view.registration.StepTwo",
    title: "Паспортные данные"
});
Ext.define("CB.locale.ru.view.registration.StepThree", {
    override: "CB.view.registration.StepThree",
    title: "Адрес"
});

Ext.define("CB.locale.ru.controller.Login", {
    override: "CB.controller.Login",
    errAlreadyRegisteredEmail: "Такой e-mail адрес уже зарегистрирован",
    errInvalidLoginOrPassword: "Вы ввели неправильный логин или пароль"
});
Ext.define("CB.locale.ru.controller.Registration", {
    override: "CB.controller.Registration",
    errPhoneUsed: "Такой номер телефона уже зарегистрирован, проверьте поля формы еще раз",
    errIdcardUsed: "Такой номер пасспорта уже зарегистрирован, проверьте поля формы еще раз"
});
Ext.define("CB.locale.ru.core.FormProcessor", {
    override: "CB.core.FormProcessor",
    errorHeader: "Неприятность",
    connectionError: "Сервер недоступен или получена ошибка, которую мы не можем обработать",
    loginInvalid: "Имя или пароль введены неправильно",
    alreadyRegistered: "Пользователь с таким e-mail адресом уже зарегистрирован"
});

Ext.define("CB.locale.ru.view.Parent", {
    override: "CB.view.Parent",
    errorHeader: 'Неприятность',
    connectionError: 'Сервер недоступен : ',
    /** Buttons */
    btnNext: 'Далее',
    btnBack: 'Назад',
    btnFinish: 'Завершить',
    /** Login page */
    fldEmail: 'Ваш e-mail адрес',
    fldPassword: 'Пароль',
    btnLogin: 'Войдите',
    btnRegister: 'или Зарегистрируйтесь',
    loginInvalid: 'Неверный логин или пароль',
    /** Main user contacts */
    fldFirst: 'Имя',
    fldMiddle: 'Отчество',
    fldLast: 'Фамилия',
    fldPhone: 'Номер мобильного',
    fldCode: 'Код мобильного',
    /** Id card */
    fldBirthDate: 'Дата рождения',
    fldIdSerial: 'Серия паспорта',
    fldIdNumber: 'Номер паспорта',
    /** Place */
    fldRegion: 'Регион',
    fldCity: 'Город',
    fldAddress: 'Адрес'
});