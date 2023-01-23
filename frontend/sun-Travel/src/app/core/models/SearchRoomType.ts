import { pair } from "../pages/dashboard/dashboard.component";
import { LocationDto } from "./LocationDto";

export interface SearchRoomType {
    start: string | null;
    end: string | null;
    location: string | null;
    pairs: pair[];
}