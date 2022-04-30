package dataAccess;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    public String request(String endpoint){
        String result = "";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.football-data.org/v2/"+ endpoint)
                .get()
                .addHeader("X-Auth-Toker",System.getenv("TOKEN"))
                .build();

        try{
            Response response = client.newCall(request).execute();
            if(response.code() == 200){
                result = response.body().string();
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return result;
    }

    public class Competition{
        public int id;
        public Area area;
        public String name;
        public String code;
        public String emblemUrl;
        public String plan;

        Season currentSeason;
        int numberOfAvailableSeasons;
        String lastUpdated;

        @Override
        public String toString(){
            return "Competition{"+
                    "id="+ id +
                    ", area="+ area +
                    ", name='"+ name + '\'' +
                    ", code='"+ code + '\'' +
                    ", emblemUrl='"+ emblemUrl + '\'' +
                    ", plan ='" + plan + '\'' +
                    ", currentSeason="+ currentSeason +
                    ", numberOfAvailableSeasons="+ numberOfAvailableSeasons +
                    ", lastUpdated='"+ lastUpdated + '\'' +
                    "}";
        }

        public String getEmblemUrl() {
                return emblemUrl;
        }
    }

    public class Area{
        public int id;
        public String name;

        @Override
        public String toString(){
            return "Area{"+
                    "id="+ id +
                    ", name="+name+
                    "}";
        }
    }

    class Season{
        int id;
        String startDate;
        String endDate;
        int currentMatchday;
        Object winner;

        @Override
        public String toString(){
            return "Season{"+
                    "id="+id+
                    ", startDate=" + startDate +
                    ", endDate="+ endDate +
                    ", currentMatchday="+ currentMatchday +
                    ", winner="+ winner.toString()+
                    "}";
        }
    }

    public static void main(String[] args){
        Manager manager = new Manager();
        String body = manager.request("competitions");

        Gson gson = new Gson();
        JsonObject jsonObject;

        jsonObject = gson.fromJson(body, JsonObject.class);
        Type competitionListType = new TypeToken<ArrayList<Competition>>(){}.getType();
        List<Competition> competitions = gson.fromJson((jsonObject.get("competitions")), competitionListType);
        System.out.println(competitions.get(0).id);
        System.out.println(competitions.get(0).area);
        System.out.println(competitions.get(0).name);
        System.out.println(competitions.get(0).currentSeason.startDate);
        System.out.println(competitions.get(5).emblemUrl);
    }
}
