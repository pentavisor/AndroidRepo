package com.example.myapplicationisbetter.data.models;

    public class UserInetInform{

        private String firstNameInet;
        private String photoInet;
        private Boolean sex;
        private String mapCoordinates;

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

        public String getMapCoordinates() {
            return mapCoordinates;
        }

        public void setMapCoordinates(String mapCoordinates) {
            this.mapCoordinates = mapCoordinates;
        }

        public UserInetInform(String firstNameInet, String photoInet, Boolean sex, String mapCoordinates) {
            this.firstNameInet = firstNameInet;
            this.photoInet = photoInet;
            this.sex = sex;
            this.mapCoordinates = mapCoordinates;
        }


    }