package com.viaeai.twitter;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import com.viaeai.twitter.MessageMapper.TwitterMessageMapper;
import com.viaeai.twitter.twitter4j.TweetService;
import com.viaeai.twitter.viaeaibotMessage.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TwitterApplicationTests {

    @Mock
    private TweetService tweetService;
    
   
    private TwitterMessageMapper messageMapper;
    
    @Mock
    private Twitter twitter;
    
    @Mock
    private Message message;
    
    @Rule
    public MockitoRule mockitoRule =MockitoJUnit.rule();
    
    @Mock
    private Status status;
    
    @Test
    public void testGetTweets() throws TwitterException {
        Mockito.when(tweetService.getLatestTweets()).thenReturn(
                new ArrayList<String>(
                        Arrays.asList("hello,hi,test".split(",")))
        );
//        List<String> tweets = tweetService.getLatestTweets();
        
        Assert.assertNotNull(tweetService.getLatestTweets());
        Assert.assertTrue(tweetService.getLatestTweets() instanceof List);
        Assert.assertTrue(tweetService.getLatestTweets().get(0).equals("hello"));
    }
    
    @Test
    public void testDirectMessageToViaeaiMessage(){
        DirectMessage dm=Mockito.mock(DirectMessage.class);
       
        Assert.assertNotNull(messageMapper.mapDirectMessageToViaeaiMessage(dm));
        Assert.assertTrue(messageMapper.mapDirectMessageToViaeaiMessage(dm) instanceof Message);
    }
    
    @Test
    public void testmapStatusToViaeaiMessage(){
          
        Assert.assertNotNull(messageMapper.mapStatusToViaeaiMessage(status));
        Assert.assertTrue(messageMapper.mapStatusToViaeaiMessage(status) instanceof Message);
    }
    
    @Test
    public void mapViaeaiMessageToDirectMessage() throws TwitterException{
          
        Assert.assertNotNull(messageMapper.mapViaeaiMessageToDirectMessage(message, twitter));
        Assert.assertTrue(messageMapper.mapViaeaiMessageToDirectMessage(message, twitter) instanceof DirectMessage);
    }
    
    @Test
    public void mapViaeaiMessageToStatusUpdate(){
          
        Assert.assertNotNull(messageMapper.mapViaeaiMessageToStatusUpdate(message, twitter));
        Assert.assertTrue(messageMapper.mapViaeaiMessageToStatusUpdate(message, twitter) instanceof StatusUpdate);
    }
    
    @Test
    public void mapViaeaiMessageWithMediaToStatusUpdate(){
          
        Assert.assertNotNull(messageMapper.mapViaeaiMessageWithMediaToStatusUpdate(message, twitter));
        Assert.assertTrue(messageMapper.mapViaeaiMessageWithMediaToStatusUpdate(message, twitter) instanceof StatusUpdate);
    }
}
