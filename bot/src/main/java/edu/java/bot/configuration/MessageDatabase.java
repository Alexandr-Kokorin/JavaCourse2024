package edu.java.bot.configuration;

@SuppressWarnings({"LineLength", "HideUtilityClassConstructor"})
public class MessageDatabase {

    public static String unidentifiedMessage = "То, что ты прислал, не моих рук дело, и во владениях моих такого нет!";
    public static String gagMessage = "Я ещё работаю над этим, не мешай!";
    public static String startMessagePart1 = """
            Приветствую!

            Я очень рад, что ты познал этот мир и пришёл ко мне. Больше нет нужды заходить и отслеживать изменения на сайтах, божья сила тебе в помощь!""";
    public static String startMessagePart2 = """
            Но прежде знакомство. Введи пожалуйста своё имя или ник, как тебе удобнее.

            Ну или я назову тебя user7352896. И не говори, что не могу, как никак богом называюсь!""";
    public static String startMessagePart3 = "Добро пожаловать в мои владения, ";
    public static String startMessagePart4 = """
            ! Дай же больше ссылок богу ссылок!

            P.s. Воспользуйся для этого магией /track""";
    public static String trackMessagePart1 = "Ещё одна ссылочка, а сколько радости для бога! Скинь мне её скорее!";
    public static String trackMessagePart2 = "*Бог ссылок благоволит вам*";
    public static String trackMessageError = "Странная эта ссылка какая-то. Дурить меня вздумал? Давай нормальную!";
    public static String untrackMessagePart1 = "Ну как же так, друг мой! Давно кары божей не видал? Ну давай, скидывай её уже сюда!";
    public static String untrackMessagePart2 = "*Бог ссылок вами очень недоволен*";
    public static String untrackMessageError = "Если уж удумал бога разгневать, так делай это нормально! Сначала владения осмотри, а потом приступай.";
    public static String helpMessage = """
            Божья помощь уже здесь!
            /start -- Помню, было такое, но можем познакомиться ещё раз.
            /track -- Добавить ссылочку и порадовать бога.
            /untrack -- Убрать ссылочку и разгневать бога.
            /list -- Осмотреть владения божьи.
            /help -- Божья помощь всем нуждающимся.""";

    public static String listMessage = "Вот они, мои хорошие! Трогать не советую, только смотреть.";
    public static String emptyListMessage = "*Владения пусты, бог негодует*";
}
