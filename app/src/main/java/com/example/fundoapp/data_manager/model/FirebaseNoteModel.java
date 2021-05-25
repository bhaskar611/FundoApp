package com.example.fundoapp.data_manager.model;

public class FirebaseNoteModel {
        private  String title;
        private  String content;
        private String id;
        private long creationTime;

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public FirebaseNoteModel(){
        }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FirebaseNoteModel(String title, String content,String id){
            this.title = title;
            this.content = content;
            this.id = id;
        }

        public  String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public  String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
}

