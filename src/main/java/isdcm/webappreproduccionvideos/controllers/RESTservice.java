/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/WebService.java to edit this template
 */
package isdcm.webappreproduccionvideos.controllers;

import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.text.MessageFormat;
import isdcm.webappreproduccionvideos.model.Video;

/**
 *
 * @author alumne
 */
@WebService(serviceName = "RESTservice")
public class RESTservice {

    private static final String DB_HOST = "jdbc:derby://localhost:1527/pr2";
    private static final String DB_USER = "pr2";
    private static final String DB_PASSWORD = "pr2";
    private static final String TABLENAME = "videos";

    @WebMethod(operationName = "getByTitle")
    public List<Video> getByTitle(@WebParam(name = "title") String title) {
        
        List<Video> videos = new ArrayList<Video>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            statement=connection.createStatement();

            String sqlStatement = MessageFormat.format("SELECT * FROM " + TABLENAME + " WHERE LOWER(TITULO) LIKE LOWER(''{0}%'')", title);

            resultSet = statement.executeQuery(sqlStatement);
            
            while(resultSet.next()){
                Video video = new Video();
   
                video.setVideo(
                        resultSet.getInt("ID"), 
                        resultSet.getString("TITLE"),
                        resultSet.getString("AUTHOR"),
                        resultSet.getDate("CREATION DATE"),
                        resultSet.getTime("DURATION"),
                        resultSet.getInt("VIEWS"),
                        resultSet.getString("DESCRIPTION"),
                        resultSet.getString("FORMAT"),
                        resultSet.getString("URL"));
                 
                videos.add(video);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return videos;
    }

    @WebMethod(operationName = "getByAuthor")
    public List<Video> getByAuthor(@WebParam(name = "author") String author) {
        List<Video> videos = new ArrayList<Video>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            statement=connection.createStatement();

            String sqlStatement = MessageFormat.format("SELECT * FROM " + TABLENAME + " WHERE LOWER(AUTHOR) LIKE LOWER(''{0}%'')", author);

            resultSet = statement.executeQuery(sqlStatement);
            
            while(resultSet.next()){
                Video video = new Video();
   
                video.setVideo(
                        resultSet.getInt("ID"), 
                        resultSet.getString("TITLE"),
                        resultSet.getString("AUTHOR"),
                        resultSet.getDate("CREATION DATE"),
                        resultSet.getTime("DURATION"),
                        resultSet.getInt("VIEWS"),
                        resultSet.getString("DESCRIPTION"),
                        resultSet.getString("FORMAT"),
                        resultSet.getString("URL"));
                 
                videos.add(video);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return videos;
    }

    @WebMethod(operationName = "getByDate")
    public List<Video> getByDate(@WebParam(name = "date") String date) {
        
        List<Video> videos = new ArrayList<Video>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection(DB_HOST, DB_USER, DB_PASSWORD);
            statement=connection.createStatement();

            String sqlStatement = MessageFormat.format("SELECT * FROM " + TABLENAME + " WHERE LOWER(CREATION_DATE) LIKE LOWER(''{0}%'')", date);

            resultSet = statement.executeQuery(sqlStatement);
            
            while(resultSet.next()){
                Video video = new Video();
   
                video.setVideo(
                        resultSet.getInt("ID"), 
                        resultSet.getString("TITLE"),
                        resultSet.getString("AUTHOR"),
                        resultSet.getDate("CREATION DATE"),
                        resultSet.getTime("DURATION"),
                        resultSet.getInt("VIEWS"),
                        resultSet.getString("DESCRIPTION"),
                        resultSet.getString("FORMAT"),
                        resultSet.getString("URL"));                        
                 
                videos.add(video);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return videos;
    }
}
