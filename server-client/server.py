from socket import *
import UpperSentence

HOST = ''
PORT = 11559
ADDR = (HOST,PORT)

ServerSocket = socket(AF_INET,SOCK_DGRAM)
ServerSocket.bind(('',11559))


connection_list = [ServerSocket]

while True :

	
    message, clientAddress = ServerSocket.recvfrom(2048)

    data = message.decode()
    print(data)
    UpperData = UpperSentence.UpperSentence(data);
    print(UpperData)
    ServerSocket.sendto(UpperData.encode(), clientAddress)

