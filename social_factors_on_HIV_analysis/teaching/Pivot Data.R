library(tidyverse)
data<-read_csv('8-402-X2016010-T1-CANPR-eng.csv')
educ_cols_count<-c("Total - Highest certificate, diploma or degree (2016 counts)"                              
             ,"No certificate, diploma or degree (2016 counts)"                                           
             ,"Secondary (high) school diploma or equivalency certificate (2016 counts)"                  
             ,"Apprenticeship or trades certificate or diploma (2016 counts)"                             
             ,"College, CEGEP or other non-university certificate or diploma (2016 counts)"               
             ,"University certificate or diploma below bachelor level (2016 counts)")
educ_cols_percent<-c("Total - Highest certificate, diploma or degree (% distribution 2016)"                      
                     ,"No certificate, diploma or degree (% distribution 2016)"                                   
                     ,"Secondary (high) school diploma or equivalency certificate (% distribution 2016)"          
                     ,"Apprenticeship or trades certificate or diploma (% distribution 2016)"                     
                     ,"College, CEGEP or other non-university certificate or diploma (% distribution 2016)"       
                     ,"University certificate or diploma below bachelor level (% distribution 2016)")           
data_pivot<-data %>% select(c("Age","Sex",educ_cols))%>% 
                       pivot_longer(cols=educ_cols_count, names_to='education',values_to="total_count")
                     
