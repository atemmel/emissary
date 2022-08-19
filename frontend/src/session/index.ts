import {useStore} from "./../store";

const sessionStorage = window.sessionStorage;

export const tryReadSessionIntoStore = () => {
	const token = sessionStorage.getItem("jwtToken");
	const userId = sessionStorage.getItem("userId");

	if(!(token && userId)) {
		return
	}

	const store = useStore();

	store.commit("setId", parseInt(userId));
	store.commit("setToken", token);
	console.log("Read from session into store");
};
