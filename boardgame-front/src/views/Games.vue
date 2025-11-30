<template>
    <Card class="card">
        <template #title>Games</template>
        <template #content>
            <DataView :value="games">
                <template #list="slotProps">
                    <div class="flex flex-col">
                        <div v-for="(game, index) in slotProps.items" :key="index">
                            <div class="flex justify-between mb-2">
                                <label class="flex mr-auto text-gray-900">
                                    <span class="text-lg font-medium">{{ game.name }}</span>
                                </label>
                                <div style=""></div>
                                <Button class="mr-1" as="router-link" :to="'/games/' + game.id">
                                    View
                                </Button>
                                <Button icon="pi pi-times" severity="secondary" v-if="canDeleteGame(game)"
                                    @click="deleteGame(game)">

                                </Button>
                            </div>
                        </div>
                    </div>
                </template>
            </DataView>
            <div class="flex items-center">
                <Button @click="createGame">Create</Button>
            </div>
        </template>
    </Card>
    <CreateGameDialog ref="createGameDialog"></CreateGameDialog>
</template>

<script setup lang="ts">
import { onMounted, ref, useTemplateRef } from 'vue'
import { type Game } from "../api/openapi";
import useApi from '../api/api';
import emitter from '~/utils/eventBus'
import { useAuthStore } from '~/stores/auth';
import CreateGameDialog from '~/components/CreateGameDialog.vue';

const authStore = useAuthStore()

const createGameDialog = useTemplateRef('createGameDialog')

var newGameName = ref('')
var games = ref<Game[]>([]);
const api = useApi()

function canDeleteGame(game: Game): boolean {
    return authStore.user.id == game.owner.id
}

async function loadGames() {
    games.value = (await api.games()).data.games;
}

async function createGame() {
    try {
        const createGameProps = await createGameDialog.value.open()
        await api.createGame({
            name: createGameProps.name,
            type: createGameProps.type
        })
        loadGames();
    } catch { }
}

function deleteGame(game: Game) {
    emitter.confirm({
        title: 'Delete game',
        message: `Delete "${game.name}" game?`,
        closed: async (result: boolean) => {
            if (!result) {
                return
            }
            await api.deleteGame(game.id, { id: game.id })
            loadGames()
        }
    })
}

onMounted(async () => {
    loadGames();
})
</script>
