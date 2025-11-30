<template>
    <div>
        <div v-for="(row, y) in gameState.field" class="flex align-middle justify-center">
            <div v-for="(cell, x) in row" :class="cellClass(cell)" v-on:click="cellClick(x, y)">
                <i :class="cellIconClass(cell)"></i>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed, PropType } from 'vue';
import { GameActionMessage, newTicTacToeSetCellAction, TicTacToeCellChanged, TicTacToeGameStateFieldEnum, type TicTacToeGameState } from '~/api/openapi'
import { StateChangeHandler } from '~/components/games/stateChangeHandler';


function cellClass(cell) {
    return {
        "tic-tac-toe-cell": true,
        "pointer-cursor": cell == TicTacToeGameStateFieldEnum.NONE && isCurrentPlayerTurn.value
    }
}

function cellIconClass(cell) {
    return {
        "pi": true,
        "pi-times": cell == TicTacToeGameStateFieldEnum.CROSS,
        "pi-circle": cell == TicTacToeGameStateFieldEnum.ZERO
    }
}

function cellClick(x: number, y: number) {
    if (!isCurrentPlayerTurn.value || porps.gameState.field[y][x] != TicTacToeGameStateFieldEnum.NONE) {
        return
    }
    console.log(`x: ${x} y: ${y}`)
    emit('performAction', newTicTacToeSetCellAction({ x: x, y: y }))
}

function handleStateChanged(stateChange: TicTacToeCellChanged) {
    const field = porps.gameState.field
    field[stateChange.y][stateChange.x] = stateChange.value as unknown as TicTacToeGameStateFieldEnum
    porps.gameState.activePlayerIndex = stateChange.activePlayerIndex
}

const isCurrentPlayerTurn = computed(() => {
    return porps.currentPlayerIndex == porps.gameState.activePlayerIndex
})

const porps = defineProps({
    gameState: {
        type: Object as PropType<TicTacToeGameState>,
        required: true
    },
    currentPlayerIndex: {
        type: Number,
        required: true
    }
})


defineExpose<StateChangeHandler>({
    handleStateChanged
})

const emit = defineEmits<{
    (e: 'performAction', action: GameActionMessage): void
}>()
</script>

<style>
.tic-tac-toe-cell {
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #00000000;
    width: 2rem;
    height: 2rem;
    border-radius: 0.3rem;
    margin: 0.2rem;
    border: solid;

}

.pointer-cursor {
    cursor: pointer;
}
</style>