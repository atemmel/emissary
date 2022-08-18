import type {InjectionKey} from "vue";
import {createStore, Store, useStore as baseUseStore } from "vuex";

export interface State {
	jwtToken: string;
}

//export const key: InjectionKey<Store<State>> = Symbol();
export const key = Symbol() as InjectionKey<Store<State>>;

export const store = createStore<State>({
	state: {
		jwtToken: "",
	},
	mutations: {
		setToken: (state: State, token: string) => {
			state.jwtToken = token;
		},
	},
});

export const useStore = () => {
	return baseUseStore(key);
};

export default store;
