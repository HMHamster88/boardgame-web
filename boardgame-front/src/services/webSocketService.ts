import {
    typeNames,
    newGameHandshakeRequest,
    type GameHandshakeRequest,
    type GameHandshakeResponse,
    type GameWebsocketMessage,
    type GameWebsocketMessageType,
    type PlayerChangedMessage,
    type JoinGameResponse,
    type GameMessageHandler,
    newJoinGameRequest,
    newUpdatePlayerMessage,
    newKickPlayerRequest,
    type GameSettings,
    newStartGameRequest,
    type GameActionMessage
} from "../api/openapi"
import { useAuthStore } from "../stores/auth"


export default function useWebSocketService() {
    var websocket: WebSocket
    const authStore = useAuthStore()

    var gameMessageHandler: GameMessageHandler

    async function sendMessage(message: any) {
        websocket.send(JSON.stringify(message))
    }

    async function sendHandshake(gameId: string) {
        if (!authStore.accessToken) {
            console.log("No auth token")
            return
        }

        const message = newGameHandshakeRequest({
            gameId: gameId,
            accessToken: authStore.accessToken
        })
        sendMessage(message)
    }

    var disconnectedHandler = () => { }

    return {
        onDisconnected(handler: () => void) {
            disconnectedHandler = handler
        },
        connect(gameId: string) {
            console.log('Connecting...')
            websocket = new WebSocket('/api/websocket')
            websocket.onopen = () => {
                sendHandshake(gameId)
                console.log('Connected.')
            }
            websocket.onclose = (ev: CloseEvent) => {
                disconnectedHandler()
            }
            websocket.onmessage = (ev: MessageEvent) => {
                const data = ev.data as string
                const json = JSON.parse(data) as GameWebsocketMessage
                (gameMessageHandler as any)['on' + json['@type']](json)
            }
        },
        disconnect() {
            websocket.close()
        },
        setGameMessageHandler(handler: GameMessageHandler) {
            gameMessageHandler = handler;
        },
        join() {
            sendMessage(newJoinGameRequest({}))
        },
        updatePlayer(updateData: { name: string, color: string }) {
            sendMessage(newUpdatePlayerMessage(updateData))
        },
        kickPlayer(userId: string) {
            sendMessage(newKickPlayerRequest({ userId: userId }))
        },
        startgame(settings: GameSettings) {
            sendMessage(newStartGameRequest({ gameSettings: settings }))
        },
        performGameAction(action: GameActionMessage) {
            sendMessage(action)
        }
    }
}