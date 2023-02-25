export interface Trip {
  id: number;
  busCompany: string;
  busLogo: string;
  busEquipments: string[];
  departureCity: string;
  destinationCity: string;
  date: string;
  departureTime: string;
  destinationTime: string;
  duration?: string;
  price: number;
  isAvailable: boolean;
  availableSeats: number[];
}
