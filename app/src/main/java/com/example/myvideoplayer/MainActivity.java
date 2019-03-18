package com.example.myvideoplayer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RetrofitAdapter retrofitAdapter;
    private RecyclerView recyclerView;
    private String videoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);

        fetchJSON();

    }

    private void fetchJSON(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RecyclerInterface.XmlURl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RecyclerInterface api = retrofit.create(RecyclerInterface.class);
       // YourObject object = apiService.getXML();
        Call<String> call = api.getString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        writeRecycler(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void writeRecycler(String response){
        System.out.println("Response ======"+response);
        try {

            DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder icBuilder;

            Document parse = null;

            //Document document=dBuilder.parse("http://sample-firetv-web-app.s3-website-us-west-2.amazonaws.com/feed_firetv.xml");
           try { icBuilder = icFactory.newDocumentBuilder();
               parse = icBuilder.parse(new InputSource(new StringReader(response)));
               // document = dBuilder.parse(response);
           }catch (ParserConfigurationException e) {
               System.out.println("Response of XML ParserConfigurationException"+e);
           } catch (SAXException e) {
               System.out.println("Response of XML SAXException"+e);
           } catch (IOException e) {
               System.out.println("Response of XML IOException"+e);
           }catch (Exception e){
               System.out.println("Response of XML"+e);
           }
            parse.getDocumentElement().normalize();
            System.out.println("Root Element ::::=="+parse.getDocumentElement().getNodeName());
            NodeList nodeList=parse.getElementsByTagName("item");
            ArrayList<ModelRecycler> modelRecyclerArrayList = new ArrayList<>();
            System.out.println("----------------------------");
            for(int i=0;i<nodeList.getLength();i++){
                Node node=nodeList.item(i);
                System.out.println("\nCurrent Element :" + node.getNodeName());

//                NodeList content=parse.getElementsByTagName("media:content");
//                Node node1=content.item(0);
                ModelRecycler product=new ModelRecycler();

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                   // Element elementO=(Element)node1;
//                    System.out.println("Title : "
//                            + eElement.getAttribute("title"));

                    product.setTitle(eElement
                            .getElementsByTagName("media:title")
                            .item(0)
                            .getTextContent());
                    System.out.println("Description : "
                            + eElement
                            .getElementsByTagName("media:description")
                            .item(0)
                            .getTextContent());
                    product.setDescription(eElement
                            .getElementsByTagName("media:description")
                            .item(0)
                            .getTextContent());
                    System.out.println("Image Thumbnail : "
                            + eElement
                            .getElementsByTagName("media:thumbnail").item(0).getAttributes().getNamedItem("url").getNodeValue());
                    product.setImgURL(eElement
                            .getElementsByTagName("media:thumbnail").item(0).getAttributes().getNamedItem("url").getNodeValue());

                    System.out.println("Video Url : "
                            + eElement
                            .getElementsByTagName("media:content").item(0).getAttributes().getNamedItem("url").getNodeValue());
                    product.setVideoUrl(eElement
                            .getElementsByTagName("media:content").item(0).getAttributes().getNamedItem("url").getNodeValue());

                }
                modelRecyclerArrayList.add(product);
            }

            retrofitAdapter = new RetrofitAdapter(this,modelRecyclerArrayList);
            recyclerView.setAdapter(retrofitAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));


        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            //getting the whole json object from the response
//            JSONObject obj = new JSONObject(response);
//            if(obj.optString("status").equals("true")){
//
//                ArrayList<ModelRecycler> modelRecyclerArrayList = new ArrayList<>();
//                JSONArray dataArray  = obj.getJSONArray("item");
//
//                for (int i = 0; i < dataArray.length(); i++) {
//
//                    ModelRecycler modelRecycler = new ModelRecycler();
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//                    JSONObject jsondataobj=dataobj.getJSONObject("content");
//                    JSONObject imgJson=jsondataobj.getJSONObject("thumbnail");
//                    modelRecycler.setVideoUrl(jsondataobj.getString("url"));
//                    modelRecycler.setImgURL(imgJson.getString("url"));
//                    modelRecycler.setTitle(jsondataobj.getString("title"));
//                    modelRecycler.setDescription(jsondataobj.getString("description"));
//
//
//                    modelRecyclerArrayList.add(modelRecycler);
//
//                }
//
//                retrofitAdapter = new RetrofitAdapter(this,modelRecyclerArrayList);
//                recyclerView.setAdapter(retrofitAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//
//            }else {
//                Toast.makeText(MainActivity.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}
