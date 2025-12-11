#!/bin/bash

# Cores para deixar o terminal bonito
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}🚀 Iniciando o Sistema de Pedidos Completo...${NC}"

# Função para encerrar tudo quando der Ctrl+C
cleanup() {
    echo -e "\n${RED}🛑 Encerrando aplicações...${NC}"
    # Mata os processos Java em background
    kill $API_PID
    kill $WORKER_PID
    
    echo -e "${RED}🐳 Parando containers do Docker...${NC}"
    docker-compose stop
    
    echo -e "${GREEN}✅ Tudo limpo. Até a próxima!${NC}"
    exit
}

# Captura o sinal de "Ctrl+C" e roda a função cleanup
trap cleanup SIGINT

# 1. Subir Infraestrutura (Docker)
echo -e "${GREEN}🐳 Subindo Kafka e Postgres...${NC}"
docker-compose up -d

echo -e "${GREEN}⏳ Aguardando 15 segundos para o Kafka respirar...${NC}"
sleep 15

# 2. Iniciar a API (Em background)
echo -e "${GREEN}☕ Iniciando Order API (Logs sendo salvos em api.log)...${NC}"
cd order-api
# O "> ../api.log 2>&1" joga os logs num arquivo para não poluir a tela
./mvnw spring-boot:run > ../api.log 2>&1 &
API_PID=$! # Guarda o ID do processo para matar depois
cd ..

# 3. Iniciar o Worker (Em background, mas mostrando log na tela)
echo -e "${GREEN}🔨 Iniciando Order Worker (Logs visíveis abaixo)...${NC}"
cd order-worker

./mvnw spring-boot:run & 
WORKER_PID=$!
cd ..

# 4. Mantém o script rodando esperando você cancelar
echo -e "${GREEN}✨ Tudo rodando! Acesse http://localhost:8080/orders ou http://localhost:9000 (Kafdrop)${NC}"
echo -e "${GREEN}📝 Pressione Ctrl+C para parar tudo.${NC}"

wait