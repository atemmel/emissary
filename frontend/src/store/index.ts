import type {InjectionKey} from "vue";
import {createStore, Store, useStore as baseUseStore } from "vuex";

export interface State {
	jwtToken: string;
	userId: number;
}

export const key = Symbol() as InjectionKey<Store<State>>;

export const store = createStore<State>({
	state: {
		jwtToken: "",
		userId: 0,
	},
	mutations: {
		setToken: (state: State, token: string) => {
			state.jwtToken = token;
		},
		setId: (state: State, id: number) => {
			state.userId = id;
		},
	},
});

export const useStore = () => {
	return baseUseStore(key);
};

export default store;
