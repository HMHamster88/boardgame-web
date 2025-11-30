<template>
    <Card v-if="game.players" header="Players">
        <template #content>
            <div class="flex">
                <Chip v-for="player in game.players" :removable="canKickPlayer(player)"
                    :class="playerClassStyle(player)">
                    <div class="flex items-cente">
                        <div class="rounded-box" :style="playerColorStyle(player)"></div>
                        <div class="ml-1 mr-1">{{ player.name }}</div>
                        <div :class="{ 'bg-red-500': !player.online, 'bg-green-500': player.online, circle: true }"
                            v-tooltip="player.online ? 'Online' : 'Offline'"></div>
                    </div>
                    <template #removeicon="">
                        <i class="pi pi-times-circle p-chip-remove-icon" @click="kickPlayer(player)" />
                    </template>
                </Chip>
                <div class="mr-auto"></div>
                <Button v-on:click="join" v-if="canJoin">Join</Button>
                <Button icon="pi pi-cog" v-on:click="editPlayer" v-if="!canJoin"></Button>
            </div>
        </template>
    </Card>
    {{ connectStatus }}

    <PlayerEditDialog ref="playerEditDialog"> </PlayerEditDialog>

    <Card v-if="showGameSetting" name="settings">
        <template #title>Settings</template>
        <template #content>
            <component :is="settingsComponent" class="tab" :settings="game.settings">

            </component>
        </template>
        <template #footer>
            <Button @click="startGame">Start Game</Button>
        </template>
    </Card>

    <Card v-if="showGameView">
        <template #content>
            <component :is="gameViewComponent" class="tab" :gameState="gameState"
                :currentPlayerIndex="currentPlayerIndex" ref="gameView" @performAction="peformGameAction">

            </component>
        </template>
    </Card>

</template>

<script setup lang="ts">
import { computed, onMounted, ref, useTemplateRef } from 'vue'
import {
    GameActionMessage,
    GameErrorMessage,
    GameFinishedMessage,
    GameHandshakeResponse,
    GameStartedMessage,
    GameState,
    GameStateChangedMessage,
    GameStatusEnum,
    GameTypeEnum,
    JoinGameResponse,
    Player,
    PlayerChangedMessage,
    PlayerKickedMessage,
    type Game
}
    from '~/api/openapi';
import useApi from '~/api/api';
import { onBeforeRouteLeave, useRoute } from 'vue-router';
import useWebSocketService from '~/services/webSocketService';
import emitter from '~/utils/eventBus'
import { useAuthStore } from '~/stores/auth';
import PlayerEditDialog from '~/components/PlayerEditDialog.vue';
import { findAndRemoveElement } from '~/utils/arrayUtils';
import TickTacToeSettings from '~/components/games/tic-tac-toe/TickTacToeSettings.vue';
import TickTacToeGameView from '~/components/games/tic-tac-toe/TickTacToeGameView.vue';
import { StateChangeHandler } from '~/components/games/stateChangeHandler';
import { debounce } from '~/utils/functionUtils';


const route = useRoute()
const api = useApi()
const webSocketService = useWebSocketService()
const authStore = useAuthStore()

const gameId = route.params['id']
const game = ref<Game>({ id: null, name: null, minPlayers: 2, maxPlayers: 2, players: [], status: null, type: null, owner: null, settings: {} })
const gameState = ref<GameState>()
const connectStatus = ref('Connecting...')
const playerEditDialog = useTemplateRef('playerEditDialog')
const gameView = useTemplateRef<StateChangeHandler>('gameView')

function playerClassStyle(player: Player) {
    return {
        "mr-1": true,
        "active-player": player == activePlayer.value
    }
}

const settingsComponents = new Map<string, any>([
    [GameTypeEnum.TIC_TAC_TOE, TickTacToeSettings]
])

const gameViewComponents = new Map<string, any>([
    [GameTypeEnum.TIC_TAC_TOE, TickTacToeGameView]
])

const gameViewComponent = computed(() => {
    if (!game.value.type) {
        return null
    }
    return gameViewComponents.get(game.value.type)
})

const showGameView = computed(() => {
    return game.value.type && game.value.status == GameStatusEnum.STARTED || game.value.status == GameStatusEnum.FINISHED
})

const settingsComponent = computed(() => {
    if (!game.value.type) {
        return null
    }
    return settingsComponents.get(game.value.type)
})

const currentPlayerIndex = computed(() => {
    return game.value.players.indexOf(currentPlayer.value)
})

const activePlayer = computed(() => {
    if (gameState.value?.activePlayerIndex == null) {
        return null
    }
    return game.value.players[gameState.value.activePlayerIndex]
})

const canJoin = computed(() => {
    return game.value.players.length < game.value.maxPlayers && !currentPlayer.value
})

const isGameOwner = computed(() => {
    return game.value.owner.id == authStore.user.id
})

const showGameSetting = computed(() => {
    return game.value.type && isGameOwner.value && game.value.status == GameStatusEnum.CREATED
})

function playerColorStyle(player: Player) {
    return {
        'background-color': player.color
    }
}

webSocketService.setGameMessageHandler({
    onPlayerChangedMessage(message: PlayerChangedMessage) {
        const player = getPlayerById(message.player.user.id)
        if (player) {
            Object.assign(player, message.player)
        } else {
            game.value.players.push(message.player)
        }
    },
    onJoinGameResponse(message: JoinGameResponse) {
        if (message.success) {
            //console.log('Joined')
        } else {
            connectStatus.value = 'Join failed'
            emitter.showToastMessage({ severity: 'error', summary: 'Failed to join', life: 3000 });
        }
    },
    onGameHandshakeResponse(message: GameHandshakeResponse) {
        if (message.success) {
            connectStatus.value = 'Connected'
            const player = currentPlayer.value
            if (player) {
                join()
            }
        } else {
            connectStatus.value = 'Connect failed'
            emitter.showToastMessage({ severity: 'error', summary: 'Failed to connect', life: 3000 });
        }
    },
    onGameErrorMessage(message: GameErrorMessage) {
        emitter.showToastMessage({ severity: 'error', summary: 'Game error: ' + message.message, life: 3000 });
    },
    onPlayerKickedMessage(message: PlayerKickedMessage) {
        findAndRemoveElement(game.value.players, player => player.user.id == message.userId);
        if (authStore.user.id == message.userId) {
            emitter.showToastMessage({ severity: 'info', summary: 'You has been kicked!', life: 3000 });
        }
    },
    onGameStartedMessage(message: GameStartedMessage) {
        game.value = message.game
        gameState.value = message.gameState
        console.log('Game started', message)
    },
    onGameStateChangedMessage(message: GameStateChangedMessage) {
        gameView.value.handleStateChanged(message.change)
    },
    onGameFinishedMessage(message: GameFinishedMessage) {
        var winnersString = !message.winners ? 'No winners' :
            'Winners: ' + message.winners.map(player => player.user.username).join(', ')
        emitter.showToastMessage({ severity: 'info', summary: 'Game finished. \r\n' + winnersString, life: 3000 });
    }
})

function peformGameAction(message: GameActionMessage) {
    webSocketService.performGameAction(message)
}

function startGame() {
    webSocketService.startgame(game.value.settings)
}

function canKickPlayer(player: Player): boolean {
    return player.user.id == authStore.user.id || game.value.owner.id == authStore.user.id
}

function kickPlayer(player: Player) {
    emitter.confirm({
        title: 'Kick Player',
        message: `Kick "${player.name}" player?`,
        closed: async (result: boolean) => {
            if (!result) {
                return
            }
            webSocketService.kickPlayer(player.user.id)
        }
    })
}

function getPlayerById(id: String): Player {
    return game.value.players.find(player => player.user.id == id)
}

const currentPlayer = computed(() => {
    return game.value.players.find(player => player.user.id == authStore.user.id)
})


async function editPlayer() {
    const player = currentPlayer.value
    try {
        const newPlayerProps = await playerEditDialog.value.open(player)
        webSocketService.updatePlayer(newPlayerProps)
        console.log(newPlayerProps)
    } catch {
    }
}

function join() {
    webSocketService.join()
}

const reconnect = debounce(5000, webSocketService.connect.bind(gameId as string), true)
var disconectedOnLeave = false;

webSocketService.onDisconnected(() => {
    if (!disconectedOnLeave) {
        emitter.showToastMessage({ severity: 'error', summary: 'Disconnected. Trying to reconnect...', life: 3000 });
        reconnect()
    }
})

onMounted(async () => {
    const data = (await api.game(gameId as string)).data;
    game.value = data.game
    gameState.value = data.gameState
    disconectedOnLeave = false;
    webSocketService.connect(gameId as string)
})

onBeforeRouteLeave(() => {
    disconectedOnLeave = true;
    webSocketService.disconnect()
})

</script>

<style>
.active-player {
    border: solid;
    border-color: red;
}
</style>