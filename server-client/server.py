from socket import *
import UpperSentence
import threading
from _thread import *

def clientSocket(connectionSocket,num):
	originMessage = connectionSocket.recv(2048)
	decodedmessage = originMessage.decode()

	UpperData = UpperSentence.UpperSentence(decodedmessage)
	connectionSocket.send(UpperData.encode())

HOST = ''
PORT = 11559
ADDR = (HOST,PORT)
BUFSIZE = 2048


ServerSocket = socket(AF_INET,SOCK_STREAM)
ServerSocket.bind(('',11559))
ServerSocket.listen(10)

while True :

	(connectionSocket, clientAddress) = ServerSocket.accept()
	num = 0
	start_new_thread(clientSocket,(connectionSocket,num))
