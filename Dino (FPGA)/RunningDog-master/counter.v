module counter(count, hex0, hex1, hex2, hex3, hex4, hex5, clk, resetn, enable);
	//SW[8] enable, SW[9] reset
	input clk;
	input resetn, enable;
	output [6:0] hex0, hex1, hex2, hex3, hex4, hex5;
	output [23:0] count;
	wire signal;
	wire [23:0] display;
	
	RateDivider rate(.pulse(signal),
					.enable(enable),
					.reset(resetn),
					.clock(clk),
					.counting(28'd49_999_999));
					
	DisplayCounter dis(.num(display), .en_able(signal), .reset_n(resetn), .clk(clk));
	
	hex_decoder h0(.hex_digit(display[3:0]), .segments(hex0));
	hex_decoder h1(.hex_digit(display[7:4]), .segments(hex1));
	hex_decoder h2(.hex_digit(display[11:8]), .segments(hex2));
	hex_decoder h3(.hex_digit(display[15:12]), .segments(hex3));
	hex_decoder h4(.hex_digit(display[19:16]), .segments(hex4));
	hex_decoder h5(.hex_digit(display[23:20]), .segments(hex5));

	assign count = display;
endmodule

module RateDivider(pulse, enable, reset, clock, counting);
	input enable, clock, reset;
	input [27:0] counting;
	output pulse;
	
	reg [27:0] out;
	
	always @(posedge clock)
	begin
		if (reset == 1'b0)
			out[27:0] <= counting[27:0];
		else if (enable == 1'b1)
			begin
				if (out == 28'd0)
					out <= counting[27:0];
				else
					out <= out - 1'd1;
			end
	end
	
	assign pulse = (out == 28'd0) ? 1 : 0;
endmodule

module DisplayCounter(num, en_able, reset_n, clk);
	input en_able, reset_n, clk;
	output reg [23:0] num;
	
	always @(posedge clk, negedge reset_n)
	begin
		if (reset_n == 1'b0)
			num <= 23'b0;
		else if (en_able == 1'b1)
			begin
				if (num == 24'b1111_1111_1111_1111_1111_1111) num <= num;
				else
					begin
						num <= num + 1'b1;
					end
			end
	end
endmodule

module hex_decoder(hex_digit, segments);
    input [3:0] hex_digit;
    output reg [6:0] segments;
   
    always @(*)
        case (hex_digit)
            4'h0: segments = 7'b100_0000;
            4'h1: segments = 7'b111_1001;
            4'h2: segments = 7'b010_0100;
            4'h3: segments = 7'b011_0000;
            4'h4: segments = 7'b001_1001;
            4'h5: segments = 7'b001_0010;
            4'h6: segments = 7'b000_0010;
            4'h7: segments = 7'b111_1000;
            4'h8: segments = 7'b000_0000;
            4'h9: segments = 7'b001_1000;
            4'hA: segments = 7'b000_1000;
            4'hB: segments = 7'b000_0011;
            4'hC: segments = 7'b100_0110;
            4'hD: segments = 7'b010_0001;
            4'hE: segments = 7'b000_0110;
            4'hF: segments = 7'b000_1110;   
            default: segments = 7'h7f;
        endcase
endmodule