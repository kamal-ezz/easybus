export interface Trip {
  id: number;
  bus: Bus;
  departureCity: string;
  destinationCity: string;
  date: string;
  departureTime: string;
  destinationTime: string;
  duration?: string;
  price: number;
  availableSeats: number[];
}

interface Bus {
  id: number;
  company: string;
  logo: string;
  equipments: string[];
}
