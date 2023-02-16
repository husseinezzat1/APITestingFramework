package pageObjectModels;

public class API {
    public enum statusCode {
        succcessStatusCode(201), okStatusCode(200), badRequestStatusCode(400), notFoundStatusCode(404);
        private int statusCode;
        private statusCode(int statusCode){
            this.statusCode= statusCode;
        }
        public int getStatusCode(){
            return statusCode;
        }
    }
}
