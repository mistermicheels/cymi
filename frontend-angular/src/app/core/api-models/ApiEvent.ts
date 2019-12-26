export interface ApiEvent {
    id: number;
    name: string;
    startTimestamp: string;
    endTimestamp: string;
    location: string;
    description?: string;
}
