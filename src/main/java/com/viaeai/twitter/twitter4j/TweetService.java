/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viaeai.twitter.twitter4j;

import com.viaeai.twitter.MessageMapper.TwitterMessageMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


@Service
public class TweetService {
    
    private final TwitterMessageMapper messageMapper = new TwitterMessageMapper();
    
    @Autowired
    private Twitter twitter;
    
    
    
    public List<String> getLatestTweets(){
        List<String> tweets = new ArrayList<>();
        try {
            ResponseList<Status> homeTimeline = twitter.getHomeTimeline();
            for (Status status : homeTimeline) {
                
                //example
                messageMapper.mapStatusToViaeaiMessage(status);
                
                tweets.add(status.getText());
            }
        } catch (TwitterException e) {
            throw new RuntimeException(e);
        }
        return tweets;
    }
}