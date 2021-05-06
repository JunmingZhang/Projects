#### Workspace setup ####
library(haven)
library(tidyverse)
library(broom)
library(arm)
# Read in the raw data (You might need to change this if you use a different dataset)
raw_data_survey <- read_dta("ns20200625.dta")
# Add the labels
raw_data_survey <- labelled::to_factor(raw_data_survey)
# Just keep some variables-make sure it exists in census data as well (except for vote intention)
reduced_data_survey <- 
  raw_data_survey %>% 
  dplyr::select(vote_2020,
         vote_intention,
         registration,
         age,
         gender,
         education,
         state,
         household_income,
         race_ethnicity)
#Adjust Data types
reduced_data_survey$age<-as.numeric(reduced_data_survey$age)
# Filter on survey data
#filter only on the people that are both registered & intented to vote (Optional, depends on your assumptions)
#(Assuming people will vote unless they explicitly say no)
filtered_data_survey<-reduced_data_survey %>% 
  dplyr::filter(registration=="Registered"&
           vote_intention!="No, I am not eligible to vote"&
           vote_intention!="No, I will not vote but I am eligible"&
           (vote_2020=="Donald Trump"|vote_2020=="Joe Biden")
  )

highincome<-c('$150,000 to $174,999','$175,000 to $199,999','$200,000 to $249,999','250,000 and above')
filtered_data_survey$income_high<-ifelse(filtered_data_survey$household_income %in%  highincome, 1, 0)
filtered_data_survey$income_high<-as.factor(filtered_data_survey$income_high)

filtered_data_survey<-na.omit(filtered_data_survey)

rm(raw_data_survey,reduced_data_survey)



#Propensity Score
propensity_score <- glm(income_high ~ age + gender + education + state+race_ethnicity, 
                        family = binomial,
                        data = filtered_data_survey)
filtered_data_survey <- 
  augment(propensity_score, 
          data = filtered_data_survey,
          type.predict = "response") %>% 
  dplyr::select(-.resid, -.std.resid, -.hat, -.sigma, -.cooksd) 

# Now we use our forecast to create matches. 
filtered_data_survey <- 
  filtered_data_survey %>% 
  arrange(.fitted, income_high)

# Here we're going to use a matching function 
# from the arm package. This finds which is the 
# closest of the ones that were not treated, to 
# each one that was treated.
filtered_data_survey$treated <- 
  if_else(filtered_data_survey$income_high == 0, 0, 1)

filtered_data_survey$treated  <- 
  as.integer(filtered_data_survey$treated )

matches <- arm::matching(z = filtered_data_survey$treated , 
                         score = filtered_data_survey$.fitted)

filtered_data_survey <- cbind(filtered_data_survey, matches)


# Now we reduce the dataset to just those that 
# are matched. We had 389 treated, so we expect 
# a dataset of 778 observations.
filtered_data_survey_matched <- 
  filtered_data_survey %>% 
  filter(match.ind != 0) %>% 
  dplyr::select(-match.ind, -pairs, -treated)

head(filtered_data_survey_matched)


# Examining the 'effect' of being treated on average
# spend in the 'usual' way.
#relevel: logistic to predict "voting for Biden"
filtered_data_survey_matched$vote_2020 <- relevel(filtered_data_survey_matched$vote_2020, ref = "Donald Trump")  

propensity_score_regression <- glm(vote_2020 ~ age + gender + education + state+race_ethnicity+income_high, 
     family = binomial,
     data = filtered_data_survey_matched)

huxtable::huxreg(propensity_score_regression)








