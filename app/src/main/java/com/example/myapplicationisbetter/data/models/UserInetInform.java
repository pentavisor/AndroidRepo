package com.example.myapplicationisbetter.data.models;

    public class UserInetInform{

        private String firstNameInet;
        private String photoInet;
        private Boolean sex;

        public String getFirstNameInet() {
            return firstNameInet;
        }

        public void setFirstNameInet(String firstNameInet) {
            this.firstNameInet = firstNameInet;
        }

        public String getPhotoInet() {
            return photoInet;
        }

        public void setPhotoInet(String photoInet) {
            this.photoInet = photoInet;
        }

        public Boolean getSex() {
            return sex;
        }

        public void setSex(Boolean sex) {
            this.sex = sex;
        }

        public UserInetInform(String firstNameInet, String photoInet, Boolean sex) {
            this.firstNameInet = firstNameInet;
            this.photoInet = photoInet;
            this.sex = sex;
        }
    }