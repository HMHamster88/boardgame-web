import type { GameStateChange } from "../../api/openapi";

export interface StateChangeHandler {
    handleStateChanged(stateChange: GameStateChange): void
}