package jru.restaurantapp.app;

/**
 * Created by Cholo Mia on 12/3/2016.
 */

public class Constants {
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";


    public static final String URL_IMAGE = "https://restaurtantapp.000webhostapp.com/images/";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "firstname";
    public static final String LAST_NAME = "lastname";
    public static final String BIRTHDAY = "birthday";
    public static final String CONTACT = "contact";
    public static final String ADDRESS = "address";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String CONFIRM_PASSWORD = "confirm_password";

    //login
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
    public static final String WRONG = "wrong";
    public static final String NOT_EXIST = "doesNotExist";
    public static final String VERIFIED = "verified";
    public static final String UNVERIFIED = "unverified";
    public static final String WRONG_PASSWORD = "wrongPassword";
    //reg
    public static final String EMAIL_EXIST = "emailExisting";

    //verify
    public static final String VER_CODE = "code";
    public static final String USER_ID = "user_id";
    public static final String EVENT_ID = "eventId";
    public static final String PACKAGE_ID = "packageId";
    public static final String ID ="id";
    public static final String LOCATION_ID = "locId";


    public static final String EDIT = "editKey";


    /*
    $user_id = $request->getParam('user_id');
    $package_id = $request->getParam('package_id');
    $event_name = $request->getParam('event_name');
    $event_date_from = $request->getParam('event_date_from');
    $event_date_to = $request->getParam('event_date_to');
    $event_description = $request->getParam('event_description');
    $event_tags = $request->getParam('event_tags');
    $loc_id = $request->getParam('loc_id');
    $image_directory = $request->getParam('image_directory');
    */
    //add event
    public static final String EVENT_USER_ID = "user_id";
    public static final String EVENT_PACKAGE_ID = "package_id";
    public static final String EVENT_NAME = "event_name";
    public static final String EVENT_DATE_FROM = "event_date_from";
    public static final String EVENT_DATE_TO = "event_date_to";
    public static final String EVENT_DESCRIPTION = "event_description";
    public static final String EVENT_TAGS = "event_tags";
    public static final String EVENT_LOC_ID = "loc_id";
    public static final String EVENT_IMAGE = "image_directory";
    public static final String FROM_INVITE_GUESTS = "fromAddGuest";

    public static final String RESPONSE_GOING = "G";
    public static final String RESPONSE_IGNORE = "I";
    public static final String RESPONSE_MAYBE = "M" ;

    public static final String VIEW_ONLY = "viewOnly";
    public static final String FIREBASE = "firebase";
}
