/* eslint-disable */
/* tslint:disable */
// @ts-nocheck
/*
 * ---------------------------------------------------------------
 * ## THIS FILE WAS GENERATED VIA SWAGGER-TYPESCRIPT-API        ##
 * ##                                                           ##
 * ## AUTHOR: acacode                                           ##
 * ## SOURCE: https://github.com/acacode/swagger-typescript-api ##
 * ---------------------------------------------------------------
 */

export const typeNames = {
  GameWebsocketMessage: "GameWebsocketMessage",
  CreateGameRequest: "CreateGameRequest",
  CreateGameResponse: "CreateGameResponse",
  Game: "Game",
  GameSettings: "GameSettings",
  Player: "Player",
  User: "User",
  SignUpRequest: "SignUpRequest",
  SignUpResponse: "SignUpResponse",
  SignInRequest: "SignInRequest",
  SignInResponse: "SignInResponse",
  RefreshTokenRequest: "RefreshTokenRequest",
  RefreshTokenResponse: "RefreshTokenResponse",
  GameListResponse: "GameListResponse",
  GameResponse: "GameResponse",
  GameState: "GameState",
  PlayerState: "PlayerState",
  PlayerKickedMessage: "PlayerKickedMessage",
  GameStartedMessage: "GameStartedMessage",
  GameHandshakeResponse: "GameHandshakeResponse",
  PlayerChangedMessage: "PlayerChangedMessage",
  JoinGameRequest: "JoinGameRequest",
  GameErrorMessage: "GameErrorMessage",
  JoinGameResponse: "JoinGameResponse",
  StartGameRequest: "StartGameRequest",
  UpdatePlayerMessage: "UpdatePlayerMessage",
  GameFinishedMessage: "GameFinishedMessage",
  GameHandshakeRequest: "GameHandshakeRequest",
  KickPlayerRequest: "KickPlayerRequest",
  GameStateChangedMessage: "GameStateChangedMessage",
  GameWebsocketMessageType: "GameWebsocketMessageType",
  GameActionMessage: "GameActionMessage",
  TicTacToeSetCellAction: "TicTacToeSetCellAction",
  TicTacToeGameSettings: "TicTacToeGameSettings",
  TicTacToeGameState: "TicTacToeGameState",
  GameStateChange: "GameStateChange",
  TicTacToeCellChanged: "TicTacToeCellChanged",
  CreateGameRequestTypeEnum: "CreateGameRequestTypeEnum",
  GameTypeEnum: "GameTypeEnum",
  GameStatusEnum: "GameStatusEnum",
  UserRoleEnum: "UserRoleEnum",
  TicTacToeGameStateFieldEnum: "TicTacToeGameStateFieldEnum",
  TicTacToeCellChangedValueEnum: "TicTacToeCellChangedValueEnum",
};

export interface GameMessageHandler {
  onPlayerKickedMessage(message: PlayerKickedMessage);
  onGameStartedMessage(message: GameStartedMessage);
  onGameHandshakeResponse(message: GameHandshakeResponse);
  onPlayerChangedMessage(message: PlayerChangedMessage);
  onGameErrorMessage(message: GameErrorMessage);
  onJoinGameResponse(message: JoinGameResponse);
  onGameFinishedMessage(message: GameFinishedMessage);
  onGameStateChangedMessage(message: GameStateChangedMessage);
}

export interface GameWebsocketMessage {
  "@type": string;
}
export function newGameWebsocketMessage(message: GameWebsocketMessage) {
  message["@type"] = typeNames.GameWebsocketMessage;
  return message;
}

export interface CreateGameRequest {
  name: string;
  type: CreateGameRequestTypeEnum;
}
export function newCreateGameRequest(message: CreateGameRequest) {
  message["@type"] = typeNames.CreateGameRequest;
  return message;
}

export interface CreateGameResponse {
  game: Game;
}
export function newCreateGameResponse(message: CreateGameResponse) {
  message["@type"] = typeNames.CreateGameResponse;
  return message;
}

export interface Game {
  id?: string;
  name: string;
  type: GameTypeEnum;
  /** @format int32 */
  minPlayers: number;
  /** @format int32 */
  maxPlayers: number;
  owner: User;
  players: Player[];
  status: GameStatusEnum;
  settings: GameSettings;
}
export function newGame(message: Game) {
  message["@type"] = typeNames.Game;
  return message;
}

export type GameSettings = any;
export function newGameSettings(message: GameSettings) {
  message["@type"] = typeNames.GameSettings;
  return message;
}

export interface Player {
  user: User;
  name: string;
  color: string;
  online: boolean;
}
export function newPlayer(message: Player) {
  message["@type"] = typeNames.Player;
  return message;
}

export interface User {
  id?: string;
  username: string;
  role: UserRoleEnum;
}
export function newUser(message: User) {
  message["@type"] = typeNames.User;
  return message;
}

export interface SignUpRequest {
  username: string;
  password: string;
}
export function newSignUpRequest(message: SignUpRequest) {
  message["@type"] = typeNames.SignUpRequest;
  return message;
}

export interface SignUpResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}
export function newSignUpResponse(message: SignUpResponse) {
  message["@type"] = typeNames.SignUpResponse;
  return message;
}

export interface SignInRequest {
  username: string;
  password: string;
}
export function newSignInRequest(message: SignInRequest) {
  message["@type"] = typeNames.SignInRequest;
  return message;
}

export interface SignInResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}
export function newSignInResponse(message: SignInResponse) {
  message["@type"] = typeNames.SignInResponse;
  return message;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}
export function newRefreshTokenRequest(message: RefreshTokenRequest) {
  message["@type"] = typeNames.RefreshTokenRequest;
  return message;
}

export interface RefreshTokenResponse {
  accessToken: string;
  refreshToken: string;
}
export function newRefreshTokenResponse(message: RefreshTokenResponse) {
  message["@type"] = typeNames.RefreshTokenResponse;
  return message;
}

export interface GameListResponse {
  games: Game[];
}
export function newGameListResponse(message: GameListResponse) {
  message["@type"] = typeNames.GameListResponse;
  return message;
}

export interface GameResponse {
  game: Game;
  gameState?: GameState;
}
export function newGameResponse(message: GameResponse) {
  message["@type"] = typeNames.GameResponse;
  return message;
}

export interface GameState {
  id?: string;
  gameId: string;
  playersStates: PlayerState[];
  /** @format int32 */
  activePlayerIndex: number;
}
export function newGameState(message: GameState) {
  message["@type"] = typeNames.GameState;
  return message;
}

export interface PlayerState {
  playerId: string;
}
export function newPlayerState(message: PlayerState) {
  message["@type"] = typeNames.PlayerState;
  return message;
}

/** For WS client */
export interface PlayerKickedMessage {
  userId?: string;
}
export function newPlayerKickedMessage(message: PlayerKickedMessage) {
  message["@type"] = typeNames.PlayerKickedMessage;
  return message;
}

/** For WS client */
export interface GameStartedMessage {
  game?: Game;
  gameState?: GameState;
}
export function newGameStartedMessage(message: GameStartedMessage) {
  message["@type"] = typeNames.GameStartedMessage;
  return message;
}

/** For WS client */
export interface GameHandshakeResponse {
  success?: boolean;
}
export function newGameHandshakeResponse(message: GameHandshakeResponse) {
  message["@type"] = typeNames.GameHandshakeResponse;
  return message;
}

/** For WS client */
export interface PlayerChangedMessage {
  player?: Player;
}
export function newPlayerChangedMessage(message: PlayerChangedMessage) {
  message["@type"] = typeNames.PlayerChangedMessage;
  return message;
}

export type JoinGameRequest = any;
export function newJoinGameRequest(message: JoinGameRequest) {
  message["@type"] = typeNames.JoinGameRequest;
  return message;
}

/** For WS client */
export interface GameErrorMessage {
  message?: string;
}
export function newGameErrorMessage(message: GameErrorMessage) {
  message["@type"] = typeNames.GameErrorMessage;
  return message;
}

/** For WS client */
export interface JoinGameResponse {
  success?: boolean;
}
export function newJoinGameResponse(message: JoinGameResponse) {
  message["@type"] = typeNames.JoinGameResponse;
  return message;
}

export interface StartGameRequest {
  gameSettings?: GameSettings;
}
export function newStartGameRequest(message: StartGameRequest) {
  message["@type"] = typeNames.StartGameRequest;
  return message;
}

export interface UpdatePlayerMessage {
  name?: string;
  color?: string;
}
export function newUpdatePlayerMessage(message: UpdatePlayerMessage) {
  message["@type"] = typeNames.UpdatePlayerMessage;
  return message;
}

/** For WS client */
export interface GameFinishedMessage {
  message?: string;
  winners?: Player[];
}
export function newGameFinishedMessage(message: GameFinishedMessage) {
  message["@type"] = typeNames.GameFinishedMessage;
  return message;
}

export interface GameHandshakeRequest {
  accessToken?: string;
  gameId?: string;
}
export function newGameHandshakeRequest(message: GameHandshakeRequest) {
  message["@type"] = typeNames.GameHandshakeRequest;
  return message;
}

export interface KickPlayerRequest {
  userId?: string;
}
export function newKickPlayerRequest(message: KickPlayerRequest) {
  message["@type"] = typeNames.KickPlayerRequest;
  return message;
}

/** For WS client */
export interface GameStateChangedMessage {
  change?: GameStateChange;
}
export function newGameStateChangedMessage(message: GameStateChangedMessage) {
  message["@type"] = typeNames.GameStateChangedMessage;
  return message;
}

export type GameWebsocketMessageType = any;
export function newGameWebsocketMessageType(message: GameWebsocketMessageType) {
  message["@type"] = typeNames.GameWebsocketMessageType;
  return message;
}

export type GameActionMessage = any;
export function newGameActionMessage(message: GameActionMessage) {
  message["@type"] = typeNames.GameActionMessage;
  return message;
}

export interface TicTacToeSetCellAction {
  /** @format int32 */
  x?: number;
  /** @format int32 */
  y?: number;
}
export function newTicTacToeSetCellAction(message: TicTacToeSetCellAction) {
  message["@type"] = typeNames.TicTacToeSetCellAction;
  return message;
}

export interface TicTacToeGameSettings {
  /** @format int32 */
  fieldSize?: number;
}
export function newTicTacToeGameSettings(message: TicTacToeGameSettings) {
  message["@type"] = typeNames.TicTacToeGameSettings;
  return message;
}

export interface TicTacToeGameState {
  id?: string;
  gameId?: string;
  playersStates?: PlayerState[];
  /** @format int32 */
  activePlayerIndex?: number;
  field?: TicTacToeGameStateFieldEnum[][];
}
export function newTicTacToeGameState(message: TicTacToeGameState) {
  message["@type"] = typeNames.TicTacToeGameState;
  return message;
}

export type GameStateChange = any;
export function newGameStateChange(message: GameStateChange) {
  message["@type"] = typeNames.GameStateChange;
  return message;
}

export interface TicTacToeCellChanged {
  /** @format int32 */
  x: number;
  /** @format int32 */
  y: number;
  value: TicTacToeCellChangedValueEnum;
  /** @format int32 */
  activePlayerIndex: number;
}
export function newTicTacToeCellChanged(message: TicTacToeCellChanged) {
  message["@type"] = typeNames.TicTacToeCellChanged;
  return message;
}

export enum CreateGameRequestTypeEnum {
  TIC_TAC_TOE = "TIC_TAC_TOE",
}

export enum GameTypeEnum {
  TIC_TAC_TOE = "TIC_TAC_TOE",
}

export enum GameStatusEnum {
  CREATED = "CREATED",
  STARTED = "STARTED",
  FINISHED = "FINISHED",
}

export enum UserRoleEnum {
  USER = "USER",
  ADMIN = "ADMIN",
}

export enum TicTacToeGameStateFieldEnum {
  NONE = "NONE",
  CROSS = "CROSS",
  ZERO = "ZERO",
}

export enum TicTacToeCellChangedValueEnum {
  NONE = "NONE",
  CROSS = "CROSS",
  ZERO = "ZERO",
}

import type {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  HeadersDefaults,
  ResponseType,
} from "axios";
import axios from "axios";

export type QueryParamsType = Record<string | number, any>;

export interface FullRequestParams
  extends Omit<AxiosRequestConfig, "data" | "params" | "url" | "responseType"> {
  /** set parameter to `true` for call `securityWorker` for this request */
  secure?: boolean;
  /** request path */
  path: string;
  /** content type of request body */
  type?: ContentType;
  /** query params */
  query?: QueryParamsType;
  /** format of response (i.e. response.json() -> format: "json") */
  format?: ResponseType;
  /** request body */
  body?: unknown;
}

export type RequestParams = Omit<
  FullRequestParams,
  "body" | "method" | "query" | "path"
>;

export interface ApiConfig<SecurityDataType = unknown>
  extends Omit<AxiosRequestConfig, "data" | "cancelToken"> {
  securityWorker?: (
    securityData: SecurityDataType | null,
  ) => Promise<AxiosRequestConfig | void> | AxiosRequestConfig | void;
  secure?: boolean;
  format?: ResponseType;
}

export enum ContentType {
  Json = "application/json",
  JsonApi = "application/vnd.api+json",
  FormData = "multipart/form-data",
  UrlEncoded = "application/x-www-form-urlencoded",
  Text = "text/plain",
}

export class HttpClient<SecurityDataType = unknown> {
  public instance: AxiosInstance;
  private securityData: SecurityDataType | null = null;
  private securityWorker?: ApiConfig<SecurityDataType>["securityWorker"];
  private secure?: boolean;
  private format?: ResponseType;

  constructor({
    securityWorker,
    secure,
    format,
    ...axiosConfig
  }: ApiConfig<SecurityDataType> = {}) {
    this.instance = axios.create({
      ...axiosConfig,
      baseURL: axiosConfig.baseURL || "http://localhost:8080",
    });
    this.secure = secure;
    this.format = format;
    this.securityWorker = securityWorker;
  }

  public setSecurityData = (data: SecurityDataType | null) => {
    this.securityData = data;
  };

  protected mergeRequestParams(
    params1: AxiosRequestConfig,
    params2?: AxiosRequestConfig,
  ): AxiosRequestConfig {
    const method = params1.method || (params2 && params2.method);

    return {
      ...this.instance.defaults,
      ...params1,
      ...(params2 || {}),
      headers: {
        ...((method &&
          this.instance.defaults.headers[
            method.toLowerCase() as keyof HeadersDefaults
          ]) ||
          {}),
        ...(params1.headers || {}),
        ...((params2 && params2.headers) || {}),
      },
    };
  }

  protected stringifyFormItem(formItem: unknown) {
    if (typeof formItem === "object" && formItem !== null) {
      return JSON.stringify(formItem);
    } else {
      return `${formItem}`;
    }
  }

  protected createFormData(input: Record<string, unknown>): FormData {
    if (input instanceof FormData) {
      return input;
    }
    return Object.keys(input || {}).reduce((formData, key) => {
      const property = input[key];
      const propertyContent: any[] =
        property instanceof Array ? property : [property];

      for (const formItem of propertyContent) {
        const isFileType = formItem instanceof Blob || formItem instanceof File;
        formData.append(
          key,
          isFileType ? formItem : this.stringifyFormItem(formItem),
        );
      }

      return formData;
    }, new FormData());
  }

  public request = async <T = any, _E = any>({
    secure,
    path,
    type,
    query,
    format,
    body,
    ...params
  }: FullRequestParams): Promise<AxiosResponse<T>> => {
    const secureParams =
      ((typeof secure === "boolean" ? secure : this.secure) &&
        this.securityWorker &&
        (await this.securityWorker(this.securityData))) ||
      {};
    const requestParams = this.mergeRequestParams(params, secureParams);
    const responseFormat = format || this.format || undefined;

    if (
      type === ContentType.FormData &&
      body &&
      body !== null &&
      typeof body === "object"
    ) {
      body = this.createFormData(body as Record<string, unknown>);
    }

    if (
      type === ContentType.Text &&
      body &&
      body !== null &&
      typeof body !== "string"
    ) {
      body = JSON.stringify(body);
    }

    return this.instance.request({
      ...requestParams,
      headers: {
        ...(requestParams.headers || {}),
        ...(type ? { "Content-Type": type } : {}),
      },
      params: query,
      responseType: responseFormat,
      data: body,
      url: path,
    });
  };
}

/**
 * @title OpenAPI definition
 * @version v0
 * @baseUrl http://localhost:8080
 */
export class Api<
  SecurityDataType extends unknown,
> extends HttpClient<SecurityDataType> {
  api = {
    /**
     * No description
     *
     * @tags game-controller
     * @name Games
     * @request GET:/api/games
     */
    games: (params: RequestParams = {}) =>
      this.request<GameListResponse, any>({
        path: `/api/games`,
        method: "GET",
        ...params,
      }),

    /**
     * No description
     *
     * @tags game-controller
     * @name CreateGame
     * @request POST:/api/games
     */
    createGame: (data: CreateGameRequest, params: RequestParams = {}) =>
      this.request<CreateGameResponse, any>({
        path: `/api/games`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * No description
     *
     * @tags auth-controller
     * @name SignUp
     * @request POST:/api/auth/signup
     */
    signUp: (data: SignUpRequest, params: RequestParams = {}) =>
      this.request<SignUpResponse, any>({
        path: `/api/auth/signup`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * No description
     *
     * @tags auth-controller
     * @name SignIn
     * @request POST:/api/auth/signin
     */
    signIn: (data: SignInRequest, params: RequestParams = {}) =>
      this.request<SignInResponse, any>({
        path: `/api/auth/signin`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * No description
     *
     * @tags auth-controller
     * @name RefreshAccessToken
     * @request POST:/api/auth/refresh
     */
    refreshAccessToken: (
      data: RefreshTokenRequest,
      params: RequestParams = {},
    ) =>
      this.request<RefreshTokenResponse, any>({
        path: `/api/auth/refresh`,
        method: "POST",
        body: data,
        type: ContentType.Json,
        ...params,
      }),

    /**
     * No description
     *
     * @tags game-controller
     * @name Game
     * @request GET:/api/games/{id}
     */
    game: (id: string, params: RequestParams = {}) =>
      this.request<GameResponse, any>({
        path: `/api/games/${id}`,
        method: "GET",
        ...params,
      }),

    /**
     * No description
     *
     * @tags game-controller
     * @name DeleteGame
     * @request DELETE:/api/games/{id}
     */
    deleteGame: (id: string, params: RequestParams = {}) =>
      this.request<void, any>({
        path: `/api/games/${id}`,
        method: "DELETE",
        ...params,
      }),
  };
}
