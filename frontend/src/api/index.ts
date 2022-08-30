import axios from "axios";
import type {AxiosInstance, AxiosResponse}from "axios";
import {useStore} from "./../store";

const url = "http://localhost:8080/";
const baseUrl = url + "api";
const authUrl = url + "auth";

export type Headers = {
	[key: string]: string
}

export class Api {
	instance: AxiosInstance;
	token: string|null;

	constructor(token: string|null, baseUrl: string) {
		this.token = token;
		this.instance = axios.create({baseURL: baseUrl});
	}

	get(resource: string): Promise<AxiosResponse> {
		return this.instance.get(resource, this.token 
			? {headers: {"Authorization": `Bearer ${this.token}`}}
			: {});
	}

	post(resource: string, data: object): Promise<AxiosResponse> {
		return this.instance.post(resource, data, this.token
			? {headers: {"Authorization": `Bearer ${this.token}`}}
			: {});
	}

	postHeaders(resource: string, data: object, headers: Headers): Promise<AxiosResponse> {
		if(this.token) {
			headers.Authorization = `Bearer ${this.token}`;
		}
		return this.instance.post(resource, data, {headers: headers});
	}
}

export const useApi = () => {
	const store = useStore();
	return new Api(store.state.jwtToken, baseUrl);
};

export const useAuthApi = () => {
	return new Api(null, authUrl);
};
