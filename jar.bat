cd target\classes

jar cvfe ..\IMClientUI.jar ^
computer_network.IM.client.IMClientUI ^
computer_network\IM\client ^
computer_network\IM\network ^
computer_network\IM\exception ^
computer_network\IM\logging

jar cvfe ..\IMServer.jar ^
computer_network.IM.server.IMServer ^
computer_network\IM\server ^
computer_network\IM\network ^
computer_network\IM\exception ^
computer_network\IM\logging
