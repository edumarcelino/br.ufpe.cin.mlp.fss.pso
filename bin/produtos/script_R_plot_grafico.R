library(plotly)

txaprendizagem01 <- txaprendizagem01[order(txaprendizagem01$epoca),]

txaprendizagem01$txaprendizagem <- as.character(txaprendizagem01$txaprendizagem)
txaprendizagem01$epoca <- as.character(txaprendizagem01$epoca)
ggplot(data=txaprendizagem01, aes(x=txaprendizagem01$acertos_medios, y=txaprendizagem01$txaprendizagem, group=txaprendizagem01$epoca, colour=txaprendizagem01$epoca)) +
 
  geom_point()


ggplotly()


