'''
Description:
	Given the genome sequence, output 6 possible ORFs
	Returns the sequence of amino acids corresponding to each ORF
	Input could contain more than one fasta file
	A fasta file can be a file containing one or more sequences.
'''

import re
import sys
import argparse
from collections import defaultdict

def readfasta(filename, seq):
	'''
	Input genomic sequence in fasta format file, output genomic sequence

	Input：
		filename：fasta format document
		seq: dictionary, store genome DNA sequence
	Output：
		seq：dictionary, store updated DNA sequence
	'''
	records = open(filename, 'r')
	for line in records:
		if line.startswith('>'):
			seqname = line.strip().split(' ')[0][1:]
			if seqname in seq:
				print (f'Same filename find in file {filename}')
				sys.exit()
		else:
			seq[seqname] += line.strip()
	return seq

def reverse(sequence):
	'''
	Input genomic sequence, output reverse complementary sequence

	Input：
	sequence：string，DNA sequence, containing ACGT
	Output：
	sequence：string，reverse complementary DNA sequence
	'''
	
	sequence = sequence.upper()
	sequence = sequence.replace('A', 't')
	sequence = sequence.replace('T', 'a')
	sequence = sequence.replace('C', 'g')
	sequence = sequence.replace('G', 'c')
	return sequence.upper()[::-1]

def dna2aa(seqname, seq, seqlen):
	'''
	Convert DNA sequence into amino acid sequence

	Input：
	seqname：string, the genome name of fasta file
	seq：string, store DNA sequence, finded by ORFfinder, started with start codon and end in stop codon
	seqlen：length of amino acids output per line
	Output：
	aaseq：string, amino acid sequence
	'''

	codon_table = {"AAA": "K", "AAC": "N", "AAG": "K", "AAT": "N", "ACA": "T", "ACC": "T", "ACG": "T", "ACT": "T",
                   "AGA": "R", "AGC": "S", "AGG": "R", "AGT": "S", "ATA": "I", "ATC": "I", "ATG": "M", "ATT": "I",
                   "CAA": "Q", "CAC": "H", "CAG": "Q", "CAT": "H", "CCA": "P", "CCC": "P", "CCG": "P", "CCT": "P",
                   "CGA": "R", "CGC": "R", "CGG": "R", "CGT": "R", "CTA": "L", "CTC": "L", "CTG": "L", "CTT": "L",
                   "GAA": "E", "GAC": "D", "GAG": "E", "GAT": "D", "GCA": "A", "GCC": "A", "GCG": "A", "GCT": "A",
                   "GGA": "G", "GGC": "G", "GGG": "G", "GGT": "G", "GTA": "V", "GTC": "V", "GTG": "V", "GTT": "V",
                   "TAC": "Y", "TAT": "Y", "TCA": "S", "TCC": "S", "TCG": "S", "TCT": "S", "TGC": "C", "TGG": "W",
                   "TGT": "C", "TTA": "L", "TTC": "F", "TTG": "L", "TTT": "F", "TAG": "*", "TAA": "*", "TGA": "*"}
	prot = ''
	for i in range(0, len(seq), 3):
		codon = seq[i:i+3]
		if codon in codon_table:
			if codon_table[codon] == '*':
				pass
			else:
				prot += codon_table[codon]
	aaseq = ''
	i = 0
	while i < len(prot):
		if aaseq == '':
			aaseq = prot[i:i+seqlen]
		else:
			aaseq = '\n'.join([aaseq, prot[i:i+seqlen]])
		i += seqlen
	return aaseq

def ORFfinder(seqname, sequence, setlength, output, frames):
	'''
	Input the name and sequence of genome,output ORFs
	Need to set the minimum length ORF,
	For example, setlength=300，means ORF is corresponding to at least 300bp DNA sequence，and more than 100 amino acids

	Input：
		seqname：string, the genome name in the input fasta file
		sequence：string, store DNA sequence
		setlength：the minimum length of ORF (length of DNA sequence)
		output：string, the name of the output file
		frames: specify which ORFs to output, default 123456
	'''

	for strand, seq in (('Forward', sequence.upper()), ('Reverse', reverse(sequence))):
		n=0 if strand == 'Forward' else 3
		for frame in range(3):
			if str(frame+1+n) in frames:
				index = frame
				count = 0
				while index < len(seq) - 6:
					match = re.match('(ATG([ATCG]{3})*?T(AG|AA|GA))', str(seq[index:]))
					if match:
						orf = match.group()
						index += len(orf)
						if len(orf) >= setlength:
							start = seq.find(orf) + 1
							aaseq = dna2aa(seqname, orf, 60)
							count += 1
							output.write(f">{seqname}_F{frame+1+n}_{count:0>4}_{strand}_{len(orf)}bp_start{start}\n{aaseq}\n")
					else:
						index += 3

if __name__ == '__main__':
	parser = argparse.ArgumentParser("")
	parser.add_argument("-i","--inputs", required = True, metavar = "", help = "one or more genome sequence file, comma separate more than one fasta file")
	parser.add_argument("-o","--output", required = True, metavar = "", help = "ORF sequence file")
	parser.add_argument("-l","--orflength", required=False, metavar = "", type = int, default = 300, help = "minimum orf length [default = 300]")
	parser.add_argument("-f","--frame", required = False, metavar = "", type = str, default = '123456', help="Set which the reading frame to output [default=123456]")
	parser.add_argument("-a","--author", required = False, action = "store_true", help = "display the author's name")
	args = parser.parse_args()
	seq = defaultdict(str)
	output = open(args.output, 'w')
	for file in args.inputs.split(','):
		seq = readfasta(file, seq)
	for key in seq:
		ORFfinder(key, seq[key], args.orflength, output, frames = args.frame)
	output.close()
	if args.author:
		print("Written by XUAN LIU.")