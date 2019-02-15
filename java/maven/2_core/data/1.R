library(gdata)

advertisingData=read.csv("Advertising.csv")
names(advertisingData)

plot(advertisingData$TV,advertisingData$Sales)
tvLM=lm(Sales~TV, data=advertisingData)
summary(tvLM)
abline (tvLM, col="red")



