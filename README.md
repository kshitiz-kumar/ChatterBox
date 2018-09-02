# ChatterBox
ChatterBox is a text messaging app that uses firebase server to use realtime database and storage.
ChatterBox uses Firebase email and password authentication to register users. The app asks for the user's
details like name, phone number ,email and the password to access the account. Also it asks for the 
profile picture to be uploaded and storing it in firebase storage.The main interface of app consists of 
a tablayout with swipeable feature, integrated with a viewpager.This layout comprises of three inflating
fragment with the help of fragment adapter.The three fragments are the profile fragment which consists of 
all the user's registered information like Profile picture, the email and phone etc. The chats fragment
consists of a listview that is populated with the help of a custom adapter.The contacts fragment also consists
of a listview that shows the list of all the added contacts.The messaging is done in another activity called as 
chatbox. Here the user can send message to the other end .First the messages are stored in common node made for the 
communication,then it is populated in the Chatbox of the respective user using the Firebase adapter that handles much 
of the functionality by itself.
