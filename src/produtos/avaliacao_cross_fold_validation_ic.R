

acertos <- read.csv2("~/Projetos/sistemas_hibridos_inteligentes_parte_2/acertos.txt", sep="")

# Apos a execucao podemos tirar a media do classificador

u <- mean(as.numeric(acertos$acertosmedios)); 
s <- sd(as.numeric(acertos$acertosmedios)); 
n <- length(as.numeric(acertos$acertosmedios))

cat("Acertos medios com intervalo de confianca (95%) [", u ,"±", (qnorm(0.975) * s / sqrt(n)), "]\n")
