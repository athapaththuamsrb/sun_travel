export interface NewTypeContractDto {
    hotelDto: { name: string };
    type: string;
    max_adults: number;
    contractList: {
        start_contract: string;
        end_contract: string;
        price: number;
        count: number;
        isExpired: boolean;
        markup: number;
        description: string;
    }[]
}
