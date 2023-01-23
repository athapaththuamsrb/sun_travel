export interface ExistTypeContractDto {
    room_type_dto: { type: string, hotelDto: { name: string } };
    start_contract: string;
    end_contract: string;
    price: number;
    count: number;
    isExpired: boolean;
    markup: number;
    description: string;
}   