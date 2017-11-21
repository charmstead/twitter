/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viaeai.twitter.MessageMapper;

import com.viaeai.twitter.viaeaibotMessage.Message;
import com.viaeai.twitter.viaeaibotMessageTypes.MessageType;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import static java.util.Objects.isNull;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import twitter4j.DirectMessage;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.URLEntity;

/**
 *
 * @author tomide
 */
//see http://twitter4j.org/en/code-examples.html
// https://dzone.com/articles/creating-custom-springboot-starter-for-twitter4j

@Component
public class TwitterMessageMapper {
   
  /**
   * This will map tweets messages from the status object to the custom
   * Message type.
   * @param status contain tweet messages
   * @return Message
   */
   public Message mapStatusToViaeaiMessage(Status status){
       
       Message message = new Message();
       
       message.setMessageType(MessageType.text)
               .setMessageId(status.getId())
               .setCreatorId(status.getUser().getId())
               .setMessage_time(status.getCreatedAt().toString())
               .setMessageBody(status.getText());
       
       
       MediaEntity[] media = status.getMediaEntities();
       URLEntity[] url = status.getURLEntities();
       
       if(!isNull(media) && media.length>0){
           message.setIsFile(true)
                   .setFileUrl(media[0].getMediaURL());
           
//           there only three types that can be returned
//            "photo", "video", "animated_gif"
           switch(media[0].getType().toLowerCase().trim()){
               
               case "video":
                   message.setMessageType(MessageType.video);
                   break;
                   
               default:
                   message.setMessageType(MessageType.image);
                   break;
           }
           return message;
       }
       
       if(isNull(url) && url.length>0){
            message.setMessageType(MessageType.site)
                    .setMessageBody(url[0].getURL());
           
       }
       return message;
   } 
   
   public Message mapDirectMessageToViaeaiMessage(DirectMessage dm){
       
       Message message = new Message();
       
       message.setMessageType(MessageType.text)
               .setMessageId(dm.getId())
               .setCreatorId(dm.getRecipientId())
               .setMessage_time(dm.getCreatedAt().toString())
               .setMessageBody(dm.getText());
       
       MediaEntity[] media = dm.getMediaEntities();
       URLEntity[] url = dm.getURLEntities();
       
       if(!isNull(media) && media.length>0){
           message.setIsFile(true)
                   .setFileUrl(media[0].getMediaURL());
           
//           there only three types that can be returned
//            "photo", "video", "animated_gif"
           switch(media[0].getType().toLowerCase().trim()){
               
               case "video":
                   message.setMessageType(MessageType.video);
                   break;
                   
               default:
                   message.setMessageType(MessageType.image);
                   break;
           }
           return message;
       }
       
       if(isNull(url) && url.length>0){
            message.setMessageType(MessageType.site)
                   .setMessageBody(url[0].getURL());
           
       }
       return message;
   }
    
   //this can be passed as parameter to the twitter.updateStatus(arg);
   public DirectMessage mapViaeaiMessageToDirectMessage(Message message,Twitter twitter) throws TwitterException{
       
       return twitter.sendDirectMessage(message.getCreatorId(), message.getBody());
       
   }
   
   //this can be passed as parameter to the twitter.updateStatus(arg);
   public StatusUpdate mapViaeaiMessageToStatusUpdate(Message message,Twitter twitter){
       
        StatusUpdate status = new StatusUpdate(message.getBody());  
        
        return status;
       
   }
   
   
   //this can be passed as parameter to the twitter.updateStatus(arg);
   public StatusUpdate mapViaeaiMessageWithMediaToStatusUpdate(Message message,Twitter twitter){
       
        StatusUpdate status = new StatusUpdate(message.getBody());  
        
        URL url = null;
       try {
            url = new URL(message.getFileURL());
       } catch (MalformedURLException ex) {
           Logger.getLogger(TwitterMessageMapper.class.getName()).log(Level.SEVERE, null, ex);
       }
       try {
          status.setMedia(message.getBody(),  url.openStream());
          return status;
       } catch (IOException ex) {
           Logger.getLogger(TwitterMessageMapper.class.getName()).log(Level.SEVERE, null, ex);
       }
       
        return status;
       
   }
}
