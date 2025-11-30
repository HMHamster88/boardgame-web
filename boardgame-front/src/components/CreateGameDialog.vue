<template>
    <Dialog v-model:visible="showDialog" modal header="Create Game" :style="{ width: '25rem' }">
        <div class="flex items-center gap-4 mb-4">
            <label for="name" class="font-semibold w-24">Name</label>
            <InputText id="name" class="flex-auto" autocomplete="off" v-model="name" />
        </div>
        <div class="flex items-center gap-4 mb-8">
            <label for="type" class="font-semibold w-24">Color</label>
            <Select id="type" v-model="type" :options="gameTypes" optionLabel="name" placeholder="Select a Type"
                class="flex-auto" />
        </div>
        <div class="flex justify-end gap-2">
            <Button type="button" label="Cancel" severity="secondary" @click="close(false)"></Button>
            <Button type="button" label="Save" @click="close(true)"></Button>
        </div>
    </Dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { CreateGameRequestTypeEnum } from '~/api/openapi'

interface GameType {
    name: String,
    value: CreateGameRequestTypeEnum
}

const showDialog = ref(false)

const name = ref('New Game')
const gameTypes = ref<GameType[]>([
    {
        name: 'Tick Tack Toe',
        value: CreateGameRequestTypeEnum.TIC_TAC_TOE
    }
])

const type = ref<GameType>(gameTypes.value[0])

interface CrateGameProps {
    name: string,
    type: CreateGameRequestTypeEnum
}

var openPromise: Promise<CrateGameProps>

var openPromiseResolve: (value: CrateGameProps | PromiseLike<CrateGameProps>) => void
var openPromiseReject: (reason?: any) => void

async function open(): Promise<CrateGameProps> {
    showDialog.value = true;
    openPromise = new Promise((resolve, reject) => {
        openPromiseResolve = resolve
        openPromiseReject = reject
    });
    return openPromise
}

function close(save: boolean) {
    if (save) {
        openPromiseResolve({
            name: name.value,
            type: type.value.value
        })
    } else {
        openPromiseReject()
    }
    showDialog.value = false
}

defineExpose({
    open
})

</script>