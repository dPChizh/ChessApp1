package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.chess.engine.board.Move.*;

//Piece = abstract class which Pawn implements
public class Pawn extends Piece {


    /* Array created highlighting possible moves a pawn can make initially.*/
    public static final int [] CANDIDATE_COORDINATE_VECTOR_MOVES = {7, 8, 9, 16};

    /* class constructor to enable class to be intializises "created" the pawn object
    SUPER is used as is parent constructor. THIS would be used to represent current class.*/
    public Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(PieceType.PAWN, piecePosition, pieceAlliance);
    }

    /*
    Specific method used to calculate the possible moves a Pawn can make. By checking Tile's availability, if pawn's
    first move, colour of piece and if pawn can be moved 2 spaces at first move.
     */
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int candidateCoordinateOffset : CANDIDATE_COORDINATE_VECTOR_MOVES) {
            final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * candidateCoordinateOffset);

            if (!BoardUtils.isValidTileCoordinate((candidateDestinationCoordinate))) {
                continue;
            }

            if (candidateCoordinateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                //TODO more work to do here(deal with pawn promotion)!!!!
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            } else if (candidateCoordinateOffset == 16 && this.isFirstMove() &&
                    BoardUtils.SECOND_ROW[this.piecePosition] && this.pieceAlliance.isBlack() ||
                    BoardUtils.SEVENTH_ROW[this.piecePosition] && this.pieceAlliance.isWhite()) {
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                    !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
                }else if (candidateCoordinateOffset == 7 &&
                        !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                         (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
                    if(board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                            //TODO ADD ATTACK MOVE METHOD
                            legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                        }
                    }
                }else if (candidateCoordinateOffset == 9 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack() ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())))){
                    if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                            //TODO ADD ATTACK MOVE METHOD
                            legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                        }
                    }

            }
        }

        return Collections.unmodifiableList(legalMoves);
    }
    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceAlliance());
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }
}
